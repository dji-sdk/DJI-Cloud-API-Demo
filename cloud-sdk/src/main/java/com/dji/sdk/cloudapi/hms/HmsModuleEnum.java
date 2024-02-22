package com.dji.sdk.cloudapi.hms;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/27
 */
public enum HmsModuleEnum {

    FLIGHT_MISSION(0),

    DEVICE_MANAGEMENT(1),

    MEDIA(2),

    HMS(3);

    private final int module;

    HmsModuleEnum(int module) {
        this.module = module;
    }

    @JsonValue
    public int getModule() {
        return module;
    }

    @JsonCreator
    public static HmsModuleEnum find(int module) {
        return Arrays.stream(values()).filter(moduleEnum -> moduleEnum.module == module).findAny()
                .orElseThrow(() -> new CloudSDKException(HmsModuleEnum.class, module));
    }
}
