package com.dji.sample.common.error;

/**
 * Live streaming related error codes. When on-demand via mqtt,
 * it can be matched with the error code information replied by the pilot.
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum LiveErrorEnum implements IErrorInfo {

    NO_AIRCRAFT(613001, "No aircraft."),

    NO_CAMERA(613002, "No camera."),

    LIVE_STREAM_ALREADY_STARTED(613003, "The camera has started live streaming."),
    
    FUNCTION_NOT_SUPPORT(613004, "The function is not supported."),
    
    STRATEGY_NOT_SUPPORT(613005, "The strategy is not supported."),
    
    NOT_IN_CAMERA_INTERFACE(613006, "The current app is not in the camera interface."),
    
    NO_FLIGHT_CONTROL(613007, "The remote control has no flight control rights and cannot respond to control commands"),

    NO_STREAM_DATA(613008, "The current app has no stream data."),
    
    TOO_FREQUENT(613009, "The operation is too frequent."),
    
    ENABLE_FAILED(613010, "Please check whether the live stream service is normal."),
    
    NO_LIVE_STREAM(613011, "There are no live stream currently."),
    
    SWITCH_NOT_SUPPORT(613012, "There is already another camera in the live stream. It's not support to switch the stream directly."),

    URL_TYPE_NOT_SUPPORTED(613013, "This url type is not supported."),

    ERROR_PARAMETERS(613014, "The live stream parameters are abnormal or incomplete."),

    NO_REPLY(613098, "No live reply received."),

    UNKNOWN(613099, "UNKNOWN");


    private String msg;

    private int code;

    LiveErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getErrorMsg() {
        return this.msg;
    }

    @Override
    public Integer getErrorCode() {
        return this.code;
    }

    /**
     * Get the corresponding enumeration object based on the error code.
     * @param code error code
     * @return enumeration object
     */
    public static LiveErrorEnum find(int code) {

        for (LiveErrorEnum errorEnum : LiveErrorEnum.class.getEnumConstants()) {
            if (errorEnum.code == code) {
                return errorEnum;
            }
        }
        return UNKNOWN;
    }
}
