package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/16
 */
public enum ControlSourceEnum {

    A("A"),

    B("B"),

    UNKNOWN("");

    private final String controlSource;

    ControlSourceEnum(String controlSource) {
        this.controlSource = controlSource;
    }

    @JsonValue
    public String getControlSource() {
        return controlSource;
    }

    @JsonCreator
    public static ControlSourceEnum find(String controlSource) {
        return Arrays.stream(values()).filter(controlSourceEnum -> controlSourceEnum.controlSource.equals(controlSource)).findAny()
                .orElseThrow(() -> new CloudSDKException(ControlSourceEnum.class, controlSource));
    }
}
