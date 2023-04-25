package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dji.sample.component.mqtt.model.ChannelName;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.component.websocket.model.BizCodeEnum;
import com.dji.sample.component.websocket.service.ISendMessageService;
import com.dji.sample.control.model.enums.DroneAuthorityEnum;
import com.dji.sample.manage.dao.IDevicePayloadMapper;
import com.dji.sample.manage.model.dto.DeviceAuthorityDTO;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.dto.DeviceDictionaryDTO;
import com.dji.sample.manage.model.dto.DevicePayloadDTO;
import com.dji.sample.manage.model.entity.DevicePayloadEntity;
import com.dji.sample.manage.model.enums.ControlSourceEnum;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.enums.UserTypeEnum;
import com.dji.sample.manage.model.receiver.DevicePayloadReceiver;
import com.dji.sample.manage.model.receiver.FirmwareVersionReceiver;
import com.dji.sample.manage.service.ICapacityCameraService;
import com.dji.sample.manage.service.IDeviceDictionaryService;
import com.dji.sample.manage.service.IDevicePayloadService;
import com.dji.sample.manage.service.IDeviceRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHeaders;
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
    private ISendMessageService sendMessageService;

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
    public Boolean savePayloadDTOs(List<DevicePayloadReceiver> payloadReceiverList) {
        if (payloadReceiverList.isEmpty()) {
            return true;
        }

        String deviceSn = payloadReceiverList.get(0).getDeviceSn();
        Optional<DeviceDTO> deviceOpt = deviceRedisService.getDeviceOnline(deviceSn);
        if (deviceOpt.isEmpty()) {
            return false;
        }
        DeviceDTO device = deviceOpt.get();
        List<DevicePayloadDTO> payloads = new ArrayList<>();

        Map<String, String> controlMap = CollectionUtils.isEmpty(device.getPayloadsList()) ?
                Collections.emptyMap() :
                device.getPayloadsList().stream()
                        .collect(Collectors.toMap(DevicePayloadDTO::getPayloadIndex, DevicePayloadDTO::getControlSource));

        for (DevicePayloadReceiver payloadReceiver : payloadReceiverList) {
            int payloadId = this.saveOnePayloadDTO(payloadReceiver);
            if (payloadId <= 0) {
                return false;
            }
            payloads.add(this.receiver2Dto(payloadReceiver));
            if (!controlMap.getOrDefault(payloadReceiver.getPayloadIndex(), "").equals(payloadReceiver.getControlSource())) {
                sendMessageService.sendBatch(device.getWorkspaceId(), UserTypeEnum.WEB.getVal(),
                                    BizCodeEnum.CONTROL_SOURCE_CHANGE.getCode(),
                                    DeviceAuthorityDTO.builder()
                                            .controlSource(payloadReceiver.getControlSource())
                                            .sn(payloadReceiver.getSn())
                                            .type(DroneAuthorityEnum.PAYLOAD)
                                            .build());
            }
        }

        if (payloads.isEmpty()) {
            payloads = this.getDevicePayloadEntitiesByDeviceSn(deviceSn);
        }
        device.setPayloadsList(payloads);

        deviceRedisService.setDeviceOnline(device);
        return true;
    }

    @Override
    public Integer saveOnePayloadDTO(DevicePayloadReceiver payloadReceiver) {
        return this.saveOnePayloadEntity(payloadDTOConvertToEntity(payloadReceiver));
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
    public void updateFirmwareVersion(FirmwareVersionReceiver receiver) {
        mapper.update(DevicePayloadEntity.builder()
                        .firmwareVersion(receiver.getFirmwareVersion())
                        .build()
                , new LambdaUpdateWrapper<DevicePayloadEntity>()
                        .eq(DevicePayloadEntity::getDeviceSn, receiver.getSn()));
    }

    /**
     * Handle payload data for devices.
     * @param payloadReceiverList
     * @param headers
     */
    @ServiceActivator(inputChannel = ChannelName.INBOUND_STATE_PAYLOAD)
    public void handleDeviceBasicPayload(List<DevicePayloadReceiver> payloadReceiverList, MessageHeaders headers) {
        if (payloadReceiverList.isEmpty()) {
            return;
        }
        String deviceSn = payloadReceiverList.get(0).getDeviceSn();
        String key = RedisConst.STATE_PAYLOAD_PREFIX + deviceSn;
        // Solve timing problems
        long last = (long) Objects.requireNonNullElse(RedisOpsUtils.get(key), 0L);
        long timestamp = headers.getTimestamp();
        if (last > timestamp) {
            return;
        }

        // Filter unsaved payload information.
        Set<String> payloadSns = this.getDevicePayloadEntitiesByDeviceSn(payloadReceiverList.get(0).getDeviceSn())
                .stream().map(DevicePayloadDTO::getPayloadSn).collect(Collectors.toSet());

        Set<String> newPayloadSns = payloadReceiverList.stream().map(DevicePayloadReceiver::getSn).collect(Collectors.toSet());
        payloadSns.removeAll(newPayloadSns);
        this.deletePayloadsByPayloadsSn(payloadSns);

        // Save the new payload information.
        boolean isSave = this.savePayloadDTOs(payloadReceiverList);
        if (isSave) {
            RedisOpsUtils.setWithExpire(key, timestamp, RedisConst.DEVICE_ALIVE_SECOND);
        }
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
                Optional.of(DeviceDomainEnum.SUB_DEVICE.getVal() == device.getDomain()
                        && !CollectionUtils.isEmpty(device.getPayloadsList())
                        && ControlSourceEnum.A.getControlSource()
                        .equals(device.getPayloadsList().stream()
                                .filter(payload -> payloadIndex.equals(payload.getPayloadIndex()))
                                .map(DevicePayloadDTO::getControlSource).findAny()
                                .orElse(ControlSourceEnum.B.getControlSource())))).orElse(true);

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
                    .payloadIndex(entity.getPayloadType() + "-" + entity.getSubType() + "-" + entity.getPayloadIndex())
                    .controlSource(entity.getControlSource());
        }
        return builder.build();
    }

    /**
     * Convert the received payload object into a database entity object.
     * @param dto   payload
     * @return
     */
    private DevicePayloadEntity payloadDTOConvertToEntity(DevicePayloadReceiver dto) {
        if (dto == null) {
            return new DevicePayloadEntity();
        }
        DevicePayloadEntity.DevicePayloadEntityBuilder builder = DevicePayloadEntity.builder();

        // The cameraIndex consists of type and subType and the index of the payload hanging on the drone.
        // type-subType-index
        String[] payloadIndexArr = dto.getPayloadIndex().split("-");
        try {
            int[] arr = Arrays.stream(payloadIndexArr)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            Optional<DeviceDictionaryDTO> dictionaryOpt = dictionaryService
                    .getOneDictionaryInfoByTypeSubType(DeviceDomainEnum.PAYLOAD.getVal(), arr[0], arr[1]);
            dictionaryOpt.ifPresent(dictionary ->
                    builder.payloadName(dictionary.getDeviceName())
                            .payloadDesc(dictionary.getDeviceDesc()));

            builder.payloadType(arr[0])
                    .subType(arr[1])
                    .payloadIndex(arr[2])
                    .controlSource(dto.getControlSource());
        } catch (NumberFormatException e) {
            builder.payloadType(-1)
                    .subType(-1)
                    .payloadIndex(-1);
        }

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