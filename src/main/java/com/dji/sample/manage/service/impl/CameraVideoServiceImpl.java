package com.dji.sample.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dji.sample.manage.dao.ICameraVideoMapper;
import com.dji.sample.manage.model.dto.CapacityVideoDTO;
import com.dji.sample.manage.model.entity.CameraVideoEntity;
import com.dji.sample.manage.model.receiver.CapacityVideoReceiver;
import com.dji.sample.manage.service.ICameraVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/19
 */
@Service
@Transactional
public class CameraVideoServiceImpl implements ICameraVideoService {

    @Autowired
    private ICameraVideoMapper mapper;

    @Override
    public List<CapacityVideoDTO> getCameraVideosByCameraId(Integer cameraId) {
        return mapper.selectList(
                new LambdaQueryWrapper<CameraVideoEntity>()
                        .eq(CameraVideoEntity::getCameraId, cameraId))
                .stream()
                .map(this::entityConvertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean deleteCameraVideosById(List<Integer> ids) {
        if (ids.isEmpty()) {
            return true;
        }
        return mapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean saveCameraVideoDTOList(List<CapacityVideoReceiver> capacityVideoReceivers, Integer cameraId) {
        for (CapacityVideoReceiver videoDTO : capacityVideoReceivers) {
            CameraVideoEntity videoEntity = videoDTOConvertToEntity(videoDTO);
            videoEntity.setCameraId(cameraId);
            int saveId = this.saveOneCameraVideoEntity(videoEntity);
            if (saveId <= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Save the live capability of the lens of this camera.
     * @param entity lens data
     * @return
     */
    private Integer saveOneCameraVideoEntity(CameraVideoEntity entity) {
        return mapper.insert(entity) > 0 ? entity.getId() : 0;
    }

    /**
     * Convert the received lens capability object into a database entity object.
     * @param dto received lens object
     * @return entity
     */
    private CameraVideoEntity videoDTOConvertToEntity(CapacityVideoReceiver dto) {
        CameraVideoEntity.CameraVideoEntityBuilder builder = CameraVideoEntity.builder();
        if (dto != null) {
            builder
                    .videoIndex(dto.getVideoIndex())
                    .videoType(dto.getVideoType());
        }
        return builder.build();
    }

    /**
     * Convert database entity objects into lens data transfer object.
     * @param entity
     * @return  data transfer object
     */
    private CapacityVideoDTO entityConvertToDto(CameraVideoEntity entity) {
        CapacityVideoDTO.CapacityVideoDTOBuilder builder = CapacityVideoDTO.builder();

        if (entity != null) {
            builder
                    .id(entity.getId())
                    .index(entity.getVideoIndex())
                    .type(entity.getVideoType());
        }
        return builder.build();
    }
}