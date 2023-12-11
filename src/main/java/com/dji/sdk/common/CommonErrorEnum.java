
package com.dji.sdk.common;

import com.dji.sdk.mqtt.events.IEventsErrorCode;
import com.dji.sdk.mqtt.services.IServicesErrorCode;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum CommonErrorEnum implements IServicesErrorCode, IEventsErrorCode, IErrorInfo {

    SUCCESS(0, "Success"),

    STATUS_NOT_SUPPORTED(314000, "The device is either uploading logs or executing a flight mission. Please try again later."),

    WRONG_PARAMETER(325001, "Cloud command parameter error. Dock unable to execute command."),

    UNKNOWN(-1, "Unknown");

    private final int code;

    private final String msg;

    CommonErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.msg;
    }

    public static CommonErrorEnum find(int code) {
        return Arrays.stream(values()).filter(error -> error.code == code).findAny().orElse(UNKNOWN);
    }
}
