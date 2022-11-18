package com.dji.sample.wayline.model.enums;

import lombok.Getter;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
@Getter
public enum WaylineMethodEnum {

    FLIGHT_TASK_PREPARE("flighttask_prepare"),

    FLIGHT_TASK_EXECUTE("flighttask_execute"),

    FLIGHT_TASK_CANCEL("flighttask_undo");

    private String method;

    WaylineMethodEnum(String method) {
        this.method = method;
    }
}
