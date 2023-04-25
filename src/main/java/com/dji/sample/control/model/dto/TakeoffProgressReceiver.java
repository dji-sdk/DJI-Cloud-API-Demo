package com.dji.sample.control.model.dto;

import com.dji.sample.control.model.enums.TakeoffStatusEnum;
import com.dji.sample.wayline.model.enums.WaylineErrorCodeEnum;
import lombok.Data;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/14
 */
@Data
public class TakeoffProgressReceiver {

    private WaylineErrorCodeEnum result;

    private TakeoffStatusEnum status;

    private String flightId;

    private String trackId;

    private Integer wayPointIndex;

}
