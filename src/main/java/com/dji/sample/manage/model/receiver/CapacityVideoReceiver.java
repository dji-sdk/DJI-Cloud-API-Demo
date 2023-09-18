package com.dji.sample.manage.model.receiver;

import com.dji.sdk.cloudapi.livestream.VideoTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
@Data
public class CapacityVideoReceiver {

    private String videoIndex;

    private VideoTypeEnum videoType;

    private List<VideoTypeEnum> switchableVideoTypes;
}