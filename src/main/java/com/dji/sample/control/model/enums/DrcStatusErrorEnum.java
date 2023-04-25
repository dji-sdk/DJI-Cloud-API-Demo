package com.dji.sample.control.model.enums;

import com.dji.sample.common.error.IErrorInfo;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/17
 */
public enum DrcStatusErrorEnum implements IErrorInfo {

    SUCCESS(0, "success"),

    MQTT_ERR(514300, "The mqtt connection error."),

    HEARTBEAT_TIMEOUT(514301, "The heartbeat times out and the dock disconnects."),

    MQTT_CERTIFICATE_ERR(514302, "The mqtt certificate is abnormal and the connection fails."),

    MQTT_LOST(514303, "The dock network is abnormal and the mqtt connection is lost."),

    MQTT_REFUSE(514304, "The dock connection to mqtt server was refused."),

    UNKNOWN(-1, "Unknown");

    String msg;

    int code;

    DrcStatusErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }

    @Override
    public Integer getErrorCode() {
        return code;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static DrcStatusErrorEnum find(int code) {
        return Arrays.stream(values()).filter(error -> error.code == code).findAny().orElse(UNKNOWN);
    }
}
