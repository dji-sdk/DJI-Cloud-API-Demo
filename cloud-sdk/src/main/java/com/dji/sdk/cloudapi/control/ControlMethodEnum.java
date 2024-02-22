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

    FLY_TO_POINT_UPDATE("fly_to_point_update"),

    TAKEOFF_TO_POINT("takeoff_to_point"),

    CAMERA_MODE_SWITCH("camera_mode_switch"),

    CAMERA_PHOTO_TAKE("camera_photo_take"),

    CAMERA_PHOTO_STOP("camera_photo_stop"),

    CAMERA_RECORDING_START("camera_recording_start"),

    CAMERA_RECORDING_STOP("camera_recording_stop"),

    CAMERA_AIM("camera_aim"),

    CAMERA_FOCAL_LENGTH_SET("camera_focal_length_set"),

    GIMBAL_RESET("gimbal_reset"),

    CAMERA_LOOK_AT("camera_look_at"),

    CAMERA_SCREEN_SPLIT("camera_screen_split"),

    PHOTO_STORAGE_SET("photo_storage_set"),

    VIDEO_STORAGE_SET("video_storage_set"),

    CAMERA_EXPOSURE_SET("camera_exposure_set"),

    CAMERA_EXPOSURE_MODE_SET("camera_exposure_mode_set"),

    CAMERA_FOCUS_MODE_SET("camera_focus_mode_set"),

    CAMERA_FOCUS_VALUE_SET("camera_focus_value_set"),

    IR_METERING_MODE_SET("ir_metering_mode_set"),

    IR_METERING_POINT_SET("ir_metering_point_set"),

    IR_METERING_AREA_SET("ir_metering_area_set"),

    CAMERA_POINT_FOCUS_ACTION("camera_point_focus_action"),

    DRONE_CONTROL("drone_control"),

    DRONE_EMERGENCY_STOP("drone_emergency_stop"),

    HEART_BEAT("heart_beat"),

    POI_MODE_ENTER("poi_mode_enter"),

    POI_MODE_EXIT("poi_mode_exit"),

    POI_CIRCLE_SPEED_SET("poi_circle_speed_set"),

    ;

    private final String method;

    ControlMethodEnum(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

}
