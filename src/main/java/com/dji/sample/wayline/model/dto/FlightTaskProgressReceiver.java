package com.dji.sample.wayline.model.dto;

import lombok.Data;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/9
 */
@Data
public class FlightTaskProgressReceiver {

    private FlightTaskProgressExt ext;

    private FLightTaskProgress progress;

    private String status;

}
