package com.dji.sample.manage.service.impl;

import com.dji.sample.manage.model.dto.CapacityVideoDTO;
import com.dji.sample.manage.model.receiver.CapacityVideoReceiver;
import com.dji.sample.manage.service.ICameraVideoService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/19
 */
@Service
//@Transactional
public class CameraVideoServiceImpl implements ICameraVideoService {

    @Override
    public CapacityVideoDTO receiver2Dto(CapacityVideoReceiver receiver) {
        CapacityVideoDTO.CapacityVideoDTOBuilder builder = CapacityVideoDTO.builder();

        if (receiver != null) {
            builder.id(UUID.randomUUID().toString())
                    .index(receiver.getVideoIndex())
                    .type(receiver.getVideoType());
        }
        return builder.build();
    }
}