package com.dji.sample.control.model.dto;

import com.dji.sample.control.model.enums.FlyToStatusEnum;
import com.dji.sample.wayline.model.enums.WaylineErrorCodeEnum;
import lombok.Data;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/14
 */
@Data
public class FlyToProgressReceiver {

    private WaylineErrorCodeEnum result;

    private FlyToStatusEnum status;

    private String flyToId;

    private Integer wayPointIndex;
}
