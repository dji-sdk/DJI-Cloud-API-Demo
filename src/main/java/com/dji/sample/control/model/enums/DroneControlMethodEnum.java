package com.dji.sample.control.model.enums;

import lombok.Getter;

/**
 * @author sean
 * @version 1.3
 * @date 2023/2/21
 */
@Getter
public enum DroneControlMethodEnum {

    FLIGHT_AUTHORITY_GRAB("flight_authority_grab"),

    PAYLOAD_AUTHORITY_GRAB("payload_authority_grab"),

    FLY_TO_POINT("fly_to_point"),

    FLY_TO_POINT_STOP("fly_to_point_stop"),

    TAKE_OFF_TO_POINT("takeoff_to_point");

    String method;

    DroneControlMethodEnum(String method) {
        this.method = method;
    }
}
