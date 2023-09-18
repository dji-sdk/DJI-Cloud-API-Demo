package com.dji.sdk.cloudapi.control;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/2
 */
public enum ControlMethodEnum {

    FLIGHT_AUTHORITY_GRAB("flight_authority_grab"),

    PAYLOAD_AUTHORITY_GRAB("payload_authority_grab"),

    DRC_MODE_ENTER("drc_mode_enter"),

    DRC_MODE_EXIT("drc_mode_exit"),

    FLY_TO_POINT("fly_to_point"),

    FLY_TO_POINT_STOP("fly_to_point_stop"),

    TAKEOFF_TO_POINT("takeoff_to_point"),

    CAMERA_MODE_SWITCH("camera_mode_switch"),

    CAMERA_PHOTO_TAKE("camera_photo_take"),

    CAMERA_RECORDING_START("camera_recording_start"),

    CAMERA_RECORDING_STOP("camera_recording_stop"),

    CAMERA_AIM("camera_aim"),

    CAMERA_FOCAL_LENGTH_SET("camera_focal_length_set"),

    GIMBAL_RESET("gimbal_reset"),

    DRONE_CONTROL("drone_control"),

    DRONE_EMERGENCY_STOP("drone_emergency_stop"),

    HEART_BEAT("heart_beat");

    private final String method;

    ControlMethodEnum(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

}
