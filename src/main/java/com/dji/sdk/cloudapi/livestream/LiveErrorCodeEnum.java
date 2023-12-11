package com.dji.sdk.cloudapi.livestream;

import com.dji.sdk.common.IErrorInfo;
import com.dji.sdk.mqtt.services.IServicesErrorCode;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum LiveErrorCodeEnum implements IServicesErrorCode, IErrorInfo {

    SUCCESS(0, "Success"),

    NO_AIRCRAFT(13001, "No aircraft."),

    NO_CAMERA(13002, "No camera."),

    LIVE_STREAM_ALREADY_STARTED(13003, "The camera has started live streaming."),

    FUNCTION_NOT_SUPPORT(13004, "The function is not supported."),

    STRATEGY_NOT_SUPPORT(13005, "The strategy is not supported."),

    NOT_IN_CAMERA_INTERFACE(13006, "The current app is not in the camera interface."),

    NO_FLIGHT_CONTROL(13007, "The remote control has no flight control rights and cannot respond to control commands"),

    NO_STREAM_DATA(13008, "The current app has no stream data."),

    TOO_FREQUENT(13009, "The operation is too frequent."),

    ENABLE_FAILED(13010, "Please check whether the live stream service is normal."),

    NO_LIVE_STREAM(13011, "There are no live stream currently."),

    SWITCH_NOT_SUPPORT(13012, "There is already another camera in the live stream. It's not support to switch the stream directly."),

    URL_TYPE_NOT_SUPPORTED(13013, "This url type is not supported."),

    ERROR_PARAMETERS(13014, "The live stream parameters are abnormal or incomplete."),

    NETWORK_CONGESTION(13015, "Please check the network."),

    ERROR_FRAME(13016, "Live decoding failed."),

    DEVICE_UNKNOWN(13099, "Unknown error inside the device."),

    UNKNOWN(-1, "UNKNOWN"),
    ;


    private final String msg;

    private final int code;

    LiveErrorCodeEnum(int code, String msg) {
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
    public static LiveErrorCodeEnum find(int code) {
        return Arrays.stream(values()).filter(codeEnum -> codeEnum.code == code).findAny().orElse(UNKNOWN);
    }

}
