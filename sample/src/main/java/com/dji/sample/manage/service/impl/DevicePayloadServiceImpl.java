package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.service.IWebSocketMessageService;
import com.dji.sample.control.model.enums.DroneAuthorityEnum;
import com.dji.sample.manage.dao.IDevicePayloadMapper;
import com.dji.sample.manage.model.dto.*;
import com.dji.sample.manage.model.entity.DevicePayloadEntity;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.service.ICapacityCameraService;
import com.dji.sample.manage.service.IDeviceDictionaryService;
import com.dji.sample.manage.service.IDevicePayloadService;
import com.dji.sample.manage.service.IDeviceRedisService;
import com.dji.sdk.cloudapi.device.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/19
 */
@Slf4j
@Service
@Transactional
public class DevicePayloadServiceImpl implements IDevicePayloadService {

    @Autowired
    private IDevicePayloadMapper mapper;

    @Autowired
    private IDeviceDictionaryService dictionaryService;

    @Autowired
    private ICapacityCameraService capacityCameraService;

    @Autowired
    private IWebSocketMessageService sendMessageService;

    @Autowired
    private IDeviceRedisService deviceRedisService;

    @Override
    public Integer checkPayloadExist(String payloadSn) {
        DevicePayloadEntity devicePayload = mapper.selectOne(
                new LambdaQueryWrapper<DevicePayloadEntity>()
                        .eq(DevicePayloadEntity::getPayloadSn, payloadSn));
        return devicePayload != null ? devicePayload.getId() : -1;
    }

    private Integer saveOnePayloadEntity(DevicePayloadEntity entity) {
        int id = this.checkPayloadExist(entity.getPayloadSn());
        // If it already exists, update the data directly.
        if (id > 0) {
            entity.setId(id);
            // For the payload of the drone itself, there is no firmware version.
            entity.setFirmwareVersion(null);
            return mapper.updateById(entity) > 0 ? entity.getId() : 0;
        }
        return mapper.insert(entity) > 0 ? entity.getId() : 0;
    }

    @Override
    public Boolean savePayloadDTOs(DeviceDTO device, List<DevicePayloadReceiver> payloadReceiverList) {
        Map<String, ControlSourceEnum> controlMap = CollectionUtils.isEmpty(device.getPayloadsList()) ?
                Collections.emptyMap() : device.getPayloadsList().stream()
                    .collect(Collectors.toMap(DevicePayloadDTO::getPayloadSn, DevicePayloadDTO::getControlSource));

        for (DevicePayloadReceiver payloadReceiver : payloadReceiverList) {
            payloadReceiver.setDeviceSn(device.getDeviceSn());
            int payloadId = this.saveOnePayloadDTO(payloadReceiver);
            if (payloadId <= 0) {
                log.error("Payload data saving failed.");
                return false;
            }
            if (controlMap.get(payloadReceiver.getSn()) != payloadReceiver.getControlSource()) {
                sendMessageService.sendBatch(device.getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                                    BizCodeEnum.CONTROL_SOURCE_CHANGE.getCode(),
                                    DeviceAuthorityDTO.builder()
                                            .controlSource(payloadReceiver.getControlSource())
                                            .sn(payloadReceiver.getSn())
                                            .type(DroneAuthorityEnum.PAYLOAD)
                                            .build());
            }
        }

        List<DevicePayloadDTO> payloads = this.getDevicePayloadEntitiesByDeviceSn(device.getDeviceSn());
        device.setPayloadsList(payloads);
        deviceRedisService.setDeviceOnline(device);
        return true;
    }

    @Override
    public Integer saveOnePayloadDTO(DevicePayloadReceiver payloadReceiver) {
        return this.saveOnePayloadEntity(receiverConvertToEntity(payloadReceiver));
    }

    @Override
    public List<DevicePayloadDTO> getDevicePayloadEntitiesByDeviceSn(String deviceSn) {
        return mapper.selectList(
                new LambdaQueryWrapper<DevicePayloadEntity>()
                        .eq(DevicePayloadEntity::getDeviceSn, deviceSn))
                .stream()
                .map(this::payloadEntityConvertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePayloadsByDeviceSn(List<String> deviceSns) {
        deviceSns.forEach(deviceSn -> {
            mapper.delete(
                    new LambdaQueryWrapper<DevicePayloadEntity>()
                            .eq(DevicePayloadEntity::getDeviceSn, deviceSn));
            capacityCameraService.deleteCapacityCameraByDeviceSn(deviceSn);
        });
    }

    @Override
    public Boolean updateFirmwareVersion(String droneSn, PayloadFirmwareVersion receiver) {
        return mapper.update(DevicePayloadEntity.builder()
                        .firmwareVersion(receiver.getFirmwareVersion()).build(),
                new LambdaUpdateWrapper<DevicePayloadEntity>()
                        .eq(DevicePayloadEntity::getDeviceSn, droneSn)
                        .eq(DevicePayloadEntity::getPayloadSn, droneSn + "-" + receiver.getPosition().getPosition())
        ) > 0;
    }

    /**
     * Handle payload data for devices.
     * @param drone
     * @param payloads
     */
    public void updatePayloadControl(DeviceDTO drone, List<DevicePayloadReceiver> payloads) {
        boolean match = payloads.stream().peek(p -> p.setSn(Objects.requireNonNullElse(p.getSn(),
                p.getDeviceSn() + "-" + p.getPayloadIndex().getPosition().getPosition())))
                .anyMatch(p -> ControlSourceEnum.UNKNOWN == p.getControlSource());
        if (match) {
            return;
        }

        if (payloads.isEmpty()) {
            drone.setPayloadsList(null);
            this.deletePayloadsByDeviceSn(List.of(drone.getDeviceSn()));
            deviceRedisService.setDeviceOnline(drone);
            return;
        }

        // Filter unsaved payload information.
        Set<String> payloadSns = this.getDevicePayloadEntitiesByDeviceSn(drone.getDeviceSn())
                .stream().map(DevicePayloadDTO::getPayloadSn).collect(Collectors.toSet());

        Set<String> newPayloadSns = payloads.stream().map(DevicePayloadReceiver::getSn).collect(Collectors.toSet());
        payloadSns.removeAll(newPayloadSns);
        this.deletePayloadsByPayloadsSn(payloadSns);

        // Save the new payload information.
        boolean isSave = this.savePayloadDTOs(drone, payloads);
        log.debug("The result of saving the payloads is {}.", isSave);
    }

    @Override
    public void deletePayloadsByPayloadsSn(Collection<String> payloadSns) {
        if (CollectionUtils.isEmpty(payloadSns)) {
            return;
        }
        mapper.delete(new LambdaUpdateWrapper<DevicePayloadEntity>()
                .or(wrapper -> payloadSns.forEach(sn -> wrapper.eq(DevicePayloadEntity::getPayloadSn, sn))));
    }

    @Override
    public Boolean checkAuthorityPayload(String deviceSn, String payloadIndex) {
        return deviceRedisService.getDeviceOnline(deviceSn).flatMap(device ->
                Optional.of(DeviceDomainEnum.DRONE == device.getDomain()
                        && !CollectionUtils.isEmpty(device.getPayloadsList())
                        && ControlSourceEnum.A ==
                        device.getPayloadsList().stream()
                                .filter(payload -> payloadIndex.equals(payload.getPayloadIndex().toString()))
                                .map(DevicePayloadDTO::getControlSource).findAny()
                                .orElse(ControlSourceEnum.B)))
                .orElse(true);
    }

    /**
     * Convert database entity objects into payload data transfer object.
     * @param entity
     * @return
     */
    private DevicePayloadDTO payloadEntityConvertToDTO(DevicePayloadEntity entity) {
        DevicePayloadDTO.DevicePayloadDTOBuilder builder = DevicePayloadDTO.builder();
        if (entity != null) {
            builder.payloadSn(entity.getPayloadSn())
                    .payloadName(entity.getPayloadName())
                    .payloadDesc(entity.getPayloadDesc())
                    .index(entity.getPayloadIndex())
                    .payloadIndex(new PayloadIndex()
                            .setType(DeviceTypeEnum.find(entity.getPayloadType()))
                            .setSubType(DeviceSubTypeEnum.find(entity.getSubType()))
                            .setPosition(PayloadPositionEnum.find(entity.getPayloadIndex())))
                    .controlSource(ControlSourceEnum.find(entity.getControlSource()));
        }
        return builder.build();
    }

    /**
     * Convert the received payload object into a database entity object.
     * @param dto   payload
     * @return
     */
    private DevicePayloadEntity receiverConvertToEntity(DevicePayloadReceiver dto) {
        if (dto == null) {
            return new DevicePayloadEntity();
        }
        DevicePayloadEntity.DevicePayloadEntityBuilder builder = DevicePayloadEntity.builder();

        // The cameraIndex consists of type and subType and the index of the payload hanging on the drone.
        // type-subType-index
        Optional<DeviceDictionaryDTO> dictionaryOpt = dictionaryService.getOneDictionaryInfoByTypeSubType(
                DeviceDomainEnum.PAYLOAD.getDomain(), dto.getPayloadIndex().getType().getType(),
                dto.getPayloadIndex().getSubType().getSubType());
        dictionaryOpt.ifPresent(dictionary ->
                builder.payloadName(dictionary.getDeviceName())
                        .payloadDesc(dictionary.getDeviceDesc()));

        builder.payloadType(dto.getPayloadIndex().getType().getType())
                .subType(dto.getPayloadIndex().getSubType().getSubType())
                .payloadIndex(dto.getPayloadIndex().getPosition().getPosition())
                .controlSource(dto.getControlSource().getControlSource());

        return builder
                .payloadSn(dto.getSn())
                .deviceSn(dto.getDeviceSn())
                .build();
    }

    private DevicePayloadDTO receiver2Dto(DevicePayloadReceiver receiver) {
        DevicePayloadDTO.DevicePayloadDTOBuilder builder = DevicePayloadDTO.builder();
        if (receiver == null) {
            return builder.build();
        }
        return builder.payloadSn(receiver.getSn())
                .payloadIndex(receiver.getPayloadIndex())
                .controlSource(receiver.getControlSource())
                .build();
    }

}