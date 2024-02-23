package com.dji.sample.map.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dji.sample.map.dao.IDeviceFlightAreaMapper;
import com.dji.sample.map.model.dto.DeviceFlightAreaDTO;
import com.dji.sample.map.model.entity.DeviceFlightAreaEntity;
import com.dji.sample.map.service.IDeviceFlightAreaService;
import com.dji.sdk.cloudapi.flightarea.FlightAreaSyncReasonEnum;
import com.dji.sdk.cloudapi.flightarea.FlightAreaSyncStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/23
 */
@Service
@Transactional
public class DeviceFlightAreaServiceImpl implements IDeviceFlightAreaService {

    @Autowired
    private IDeviceFlightAreaMapper mapper;

    @Override
    public Optional<DeviceFlightAreaDTO> getDeviceFlightAreaByDevice(String workspaceId, String deviceSn) {
        return Optional.ofNullable(mapper.selectOne(Wrappers.lambdaQuery(DeviceFlightAreaEntity.class)
                        .eq(DeviceFlightAreaEntity::getWorkspaceId, workspaceId)
                        .eq(DeviceFlightAreaEntity::getDeviceSn, deviceSn)))
                .map(this::entity2Dto);
    }

    @Override
    public Boolean updateDeviceFile(DeviceFlightAreaDTO deviceFile) {
        return mapper.update(dto2Entity(deviceFile),
                Wrappers.lambdaUpdate(DeviceFlightAreaEntity.class)
                        .eq(DeviceFlightAreaEntity::getWorkspaceId, deviceFile.getWorkspaceId())
                        .eq(DeviceFlightAreaEntity::getDeviceSn, deviceFile.getDeviceSn())) > 0;
    }

    @Override
    public Boolean updateOrSaveDeviceFile(DeviceFlightAreaDTO deviceFile) {
        if (getDeviceFlightAreaByDevice(deviceFile.getWorkspaceId(), deviceFile.getDeviceSn()).isPresent()) {
            return updateDeviceFile(deviceFile);
        }
        DeviceFlightAreaEntity entity = dto2Entity(deviceFile);
        entity.setFileId(UUID.randomUUID().toString());
        return mapper.insert(entity) > 0;
    }

    private DeviceFlightAreaEntity dto2Entity(DeviceFlightAreaDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return DeviceFlightAreaEntity.builder()
                .deviceSn(dto.getDeviceSn())
                .workspaceId(dto.getWorkspaceId())
                .fileId(dto.getFileId())
                .syncCode(dto.getSyncCode().getReason())
                .syncStatus(dto.getSyncStatus().getStatus())
                .build();
    }

    private DeviceFlightAreaDTO entity2Dto(DeviceFlightAreaEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        FlightAreaSyncReasonEnum syncCodeEnum = FlightAreaSyncReasonEnum.find(entity.getSyncCode());
        return DeviceFlightAreaDTO.builder()
                .deviceSn(entity.getDeviceSn())
                .workspaceId(entity.getWorkspaceId())
                .fileId(entity.getFileId())
                .syncCode(syncCodeEnum)
                .syncStatus(FlightAreaSyncStatusEnum.find(entity.getSyncStatus()))
                .syncMsg(syncCodeEnum.getMsg())
                .build();
    }


}
