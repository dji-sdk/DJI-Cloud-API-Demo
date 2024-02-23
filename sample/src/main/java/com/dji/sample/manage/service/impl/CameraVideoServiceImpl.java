package com.dji.sample.manage.service.impl;

import com.dji.sample.manage.model.dto.CapacityVideoDTO;
import com.dji.sample.manage.model.receiver.CapacityVideoReceiver;
import com.dji.sample.manage.service.ICameraVideoService;
import com.dji.sdk.cloudapi.livestream.VideoTypeEnum;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/19
 */
@Service
public class CameraVideoServiceImpl implements ICameraVideoService {

    @Override
    public CapacityVideoDTO receiver2Dto(CapacityVideoReceiver receiver) {
        if (null == receiver) {
            return null;
        }
        CapacityVideoDTO.CapacityVideoDTOBuilder builder = CapacityVideoDTO.builder()
                .id(UUID.randomUUID().toString())
                .index(receiver.getVideoIndex())
                .type(receiver.getVideoType().getType());
        if (null != receiver.getSwitchableVideoTypes()) {
            builder.switchVideoTypes(receiver.getSwitchableVideoTypes().stream()
                    .map(VideoTypeEnum::getType).collect(Collectors.toList()));
        }
        return builder.build();
    }
}