package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dji.sample.manage.dao.ICapacityCameraMapper;
import com.dji.sample.manage.model.dto.CapacityCameraDTO;
import com.dji.sample.manage.model.dto.CapacityVideoDTO;
import com.dji.sample.manage.model.dto.DeviceDictionaryDTO;
import com.dji.sample.manage.model.entity.CapacityCameraEntity;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.receiver.CapacityCameraReceiver;
import com.dji.sample.manage.service.ICameraVideoService;
import com.dji.sample.manage.service.ICapacityCameraService;
import com.dji.sample.manage.service.IDeviceDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sean.zhou
 * @date 2021/11/19
 * @version 0.1
 */
@Service
@Transactional
public class CapacityCameraServiceImpl implements ICapacityCameraService {

    @Autowired
    private ICapacityCameraMapper mapper;

    @Autowired
    private ICameraVideoService cameraVideoService;

    @Autowired
    private IDeviceDictionaryService dictionaryService;

    @Override
    public List<CapacityCameraDTO> getCapacityCameraByDeviceSn(String deviceSn) {
        List<CapacityCameraDTO> capacityCamerasList = mapper.selectList(
                new LambdaQueryWrapper<CapacityCameraEntity>()
                        .eq(CapacityCameraEntity::getDeviceSn, deviceSn))
                .stream()
                .map(this::entityConvertToDto)
                .collect(Collectors.toList());
        capacityCamerasList.forEach(capacityCamera -> {
            // Set the lens data for this camera.
            capacityCamera.setVideosList(
                    cameraVideoService.getCameraVideosByCameraId(capacityCamera.getId()));
        });

        return capacityCamerasList;
    }

    @Override
    public Boolean checkExist(String deviceSn, String cameraIndex) {
        return mapper.selectOne(
                new LambdaQueryWrapper<CapacityCameraEntity>()
                        .eq(CapacityCameraEntity::getDeviceSn, deviceSn)
                        .eq(CapacityCameraEntity::getCameraIndex, cameraIndex)
                        .last(" limit 1")) != null;
    }

    @Override
    public Boolean deleteCapacityCameraByDeviceSn(String deviceSn) {

        List<CapacityCameraDTO> capacityCamerasList = this.getCapacityCameraByDeviceSn(deviceSn);
        // Return directly if no data exists in the database.
        if (capacityCamerasList.isEmpty()) {
            return true;
        }

        List<Integer> cameraIds = capacityCamerasList
                .stream()
                .map(CapacityCameraDTO::getId)
                .collect(Collectors.toList());

        List<Integer> videoIds = capacityCamerasList
                .stream()
                .flatMap(camera -> camera.getVideosList().stream())
                .map(CapacityVideoDTO::getId)
                .collect(Collectors.toList());

        return mapper.deleteBatchIds(cameraIds) > 0 && cameraVideoService.deleteCameraVideosById(videoIds);
    }

    @Override
    public Boolean saveCapacityCameraReceiverList(List<CapacityCameraReceiver> capacityCameraReceivers, String deviceSn) {
        for (CapacityCameraReceiver cameraDTO : capacityCameraReceivers) {
            CapacityCameraEntity cameraEntity = receiverConvertToEntity(cameraDTO);
            cameraEntity.setDeviceSn(deviceSn);
            int cameraId = this.saveOneCapacityCameraEntity(cameraEntity);
            if (cameraId <= 0) {
                continue;
            }
            boolean saveVideo = cameraVideoService.saveCameraVideoDTOList(
                    cameraDTO.getVideosList(), cameraId);

            if (!saveVideo) {
                return false;
            }
        }
        return true;
    }

    /**
     * Save the camera live capability data of the device.
     * @param cameraEntity
     * @return
     */
    private Integer saveOneCapacityCameraEntity(CapacityCameraEntity cameraEntity) {
        boolean exist = checkExist(
                cameraEntity.getDeviceSn(), cameraEntity.getCameraIndex());
        if (exist) {
            return -1;
        }
        return mapper.insert(cameraEntity) > 0 ? cameraEntity.getId() : 0;
    }

    /**
     *  Convert the received camera capability object into a database entity object.
     * @param receiver
     * @return
     */
    private CapacityCameraEntity receiverConvertToEntity(CapacityCameraReceiver receiver) {
        CapacityCameraEntity.CapacityCameraEntityBuilder builder = CapacityCameraEntity.builder();
        if (receiver == null) {
            return builder.build();
        }
        int[] indexArr = Arrays.stream(receiver.getCameraIndex().split("-"))
                .map(Integer::valueOf)
                .mapToInt(Integer::intValue)
                .toArray();
        // The cameraIndex consists of type and subType and the index of the payload hanging on the drone.
        // type-subType-index
        if (indexArr.length == 3) {
            Optional<DeviceDictionaryDTO> dictionaryOpt = dictionaryService
                    .getOneDictionaryInfoByDomainTypeSubType(
                            DeviceDomainEnum.PAYLOAD.getVal(), indexArr[0], indexArr[1]);
            dictionaryOpt.ifPresent(dictionary ->
                    builder.name(dictionary.getDeviceName()));
        }
        return builder
                .availableVideoNumber(receiver.getAvailableVideoNumber())
                .coexistVideoNumberMax(receiver.getCoexistVideoNumberMax())
                .cameraIndex(receiver.getCameraIndex())
                .build();
    }

    /**
     * Convert database entity objects into camera data transfer object.
     * @param entity
     * @return
     */
    private CapacityCameraDTO entityConvertToDto(CapacityCameraEntity entity) {
        CapacityCameraDTO.CapacityCameraDTOBuilder builder = CapacityCameraDTO.builder();

        if (entity != null) {
            builder
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .deviceSn(entity.getDeviceSn())
                .index(entity.getCameraIndex())
                .build();
        }
        return builder.build();
    }
}