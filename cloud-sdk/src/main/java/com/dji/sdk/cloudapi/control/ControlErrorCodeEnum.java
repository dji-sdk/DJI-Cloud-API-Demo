package com.dji.sdk.cloudapi.control;

import com.dji.sdk.common.IErrorInfo;
import com.dji.sdk.mqtt.events.IEventsErrorCode;
import com.dji.sdk.mqtt.services.IServicesErrorCode;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum ControlErrorCodeEnum implements IServicesErrorCode, IEventsErrorCode, IErrorInfo {

    SETTING_RTH_FAILED(327000, "Height of return to home setting failed."),

    SETTING_LOST_ACTION_FAILED(327001, "Signal lost action setting failed."),

    OBTAIN_CONTROL_FAILED(327002, "Failed to obtain control."),

    DEVICE_OFFLINE(327003, "Failed to obtain control. Device offline."),

    DRAG_LIVESTREAM_VIEW_FAILED(327004, "Failed to drag livestream view."),

    AIM_FAILED(327005, "Failed to double tab to be AIM."),

    TAKE_PHOTO_FAILED(327006, "Failed to take photo."),

    START_RECORDING_FAILED(327007, "Failed to start recording."),

    STOP_RECORDING_FAILED(327008, "Failed to stop recording."),

    SWITCH_CAMERA_MODE_FAILED(327009, "Failed to switch camera modes."),

    ZOOM_CAMERA_ZOOM_FAILED(327010, "Failed to zoom in/out with zoom camera."),

    IR_CAMERA_ZOOM_FAILED(327011, "Failed to zoom in/out with IR camera."),

    DEVICE_LOCK(327012, "Failed to obtain control. Device is locked."),

    SETTING_WAYLINE_LOST_ACTION_FAILED(327013, "Wayline signal lost action setting failed."),

    GIMBAL_REACH_LIMIT(327014, "Gimbal reached movement limit."),

    WRONG_LENS_TYPE(327015, "Invalid camera lens type."),


    DRC_ABNORMAL(514300, "DRC abnormal."),

    DRC_HEARTBEAT_TIMED_OUT(514301, "DRC heartbeat timed out."),

    DRC_CERTIFICATE_ABNORMAL(514302, "DRC certificate is abnormal."),

    DRC_LINK_LOST(514303, "DRC link is lost."),

    DRC_LINK_REFUSED(514304, "DRC link is refused."),

    UNKNOWN(-1, "UNKNOWN"),

    ;


    private final String msg;

    private final int code;

    ControlErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    /**
     * @param code error code
     * @return enumeration object
     */
    public static ControlErrorCodeEnum find(int code) {
        return Arrays.stream(values()).filter(codeEnum -> codeEnum.code == code).findAny().orElse(UNKNOWN);
    }

}
