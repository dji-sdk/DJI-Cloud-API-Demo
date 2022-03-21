package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dji.sample.manage.dao.IDevicePayloadMapper;
import com.dji.sample.manage.model.dto.DeviceDictionaryDTO;
import com.dji.sample.manage.model.dto.DevicePayloadDTO;
import com.dji.sample.manage.model.entity.DevicePayloadEntity;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.receiver.DevicePayloadReceiver;
import com.dji.sample.manage.service.ICapacityCameraService;
import com.dji.sample.manage.service.IDeviceDictionaryService;
import com.dji.sample.manage.service.IDevicePayloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/19
 */
@Service
@Transactional
public class DevicePayloadServiceImpl implements IDevicePayloadService {

    @Autowired
    private IDevicePayloadMapper mapper;

    @Autowired
    private IDeviceDictionaryService dictionaryService;

    @Autowired
    private ICapacityCameraService capacityCameraService;

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
            return mapper.updateById(entity);
        }
        return mapper.insert(entity) > 0 ? entity.getId() : 0;
    }

    @Override
    public Boolean savePayloadDTOs(List<DevicePayloadReceiver> payloadReceiverList) {
        for (DevicePayloadReceiver payloadReceiver : payloadReceiverList) {
            int payloadId = this.saveOnePayloadDTO(payloadReceiver);
            if (payloadId <= 0) {
                return false;
            }
        }
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
                    .payloadIndex(entity.getPayloadIndex());
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
                    .map(Integer::valueOf)
                    .mapToInt(Integer::intValue)
                    .toArray();

            if (arr.length == 3) {
                Optional<DeviceDictionaryDTO> dictionaryOpt = dictionaryService
                        .getOneDictionaryInfoByDomainTypeSubType(DeviceDomainEnum.PAYLOAD.getVal(),
                                arr[0], arr[1]);
                dictionaryOpt.ifPresent(dictionary ->
                        builder.payloadName(dictionary.getDeviceName())
                                .payloadDesc(dictionary.getDeviceDesc()));

            }
            builder.payloadType(arr[0])
                    .subType(arr[1])
                    .payloadIndex(arr[2]);
        } catch (NumberFormatException e) {
            builder.payloadType(Integer.valueOf(payloadIndexArr[0]))
                    .subType(-1)
                    .payloadIndex(Integer.valueOf(payloadIndexArr[2]));
        }

        return builder
                .payloadSn(dto.getSn())
                .version(dto.getVersion())
                .deviceSn(dto.getSn()
                        .substring(0,
                                dto.getSn().indexOf("-")))
                .build();
    }

}