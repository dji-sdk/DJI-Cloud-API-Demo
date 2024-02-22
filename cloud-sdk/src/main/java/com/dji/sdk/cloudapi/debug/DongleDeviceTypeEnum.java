package com.dji.sdk.cloudapi.debug;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public enum DongleDeviceTypeEnum {

    DOCK("dock"),

    DRONE("drone"),

    ;

    private final String type;

    DongleDeviceTypeEnum(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator
    public static DongleDeviceTypeEnum find(String type) {
        return Arrays.stream(values()).filter(typeEnum -> typeEnum.type.equals(type)).findAny()
            .orElseThrow(() -> new CloudSDKException(DongleDeviceTypeEnum.class, type));
    }
}
