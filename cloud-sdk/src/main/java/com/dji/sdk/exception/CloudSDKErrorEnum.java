package com.dji.sdk.exception;

import com.dji.sdk.common.IErrorInfo;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/23
 */
public enum CloudSDKErrorEnum implements IErrorInfo {

    NOT_REGISTERED(210001, "Device is not registered."),

    INVALID_PARAMETER(210002, "Invalid parameter."),

    DEVICE_TYPE_NOT_SUPPORT(210003, "The current type of the device does not support this function."),

    DEVICE_VERSION_NOT_SUPPORT(210004, "The current version of the device does not support this function."),

    DEVICE_PROPERTY_NOT_SUPPORT(210005, "The current device does not support this feature."),

    MQTT_PUBLISH_ABNORMAL(211001, "The sending of mqtt message is abnormal."),

    WEBSOCKET_PUBLISH_ABNORMAL(212001, "The sending of webSocket message is abnormal."),

    WRONG_DATA(220001, "Data exceeds limit."),

    UNKNOWN(299999, "sdk unknown"),
    ;

    private final int code;

    private final String message;

    CloudSDKErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
