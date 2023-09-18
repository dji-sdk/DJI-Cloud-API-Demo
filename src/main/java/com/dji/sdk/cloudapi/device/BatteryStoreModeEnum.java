package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
public enum BatteryStoreModeEnum {

    PLAN(1),

    EMERGENCY(2);

    private final int mode;

    BatteryStoreModeEnum(int mode) {
        this.mode = mode;
    }

    @JsonValue
    public int getMode() {
        return mode;
    }

    @JsonCreator
    public static BatteryStoreModeEnum find(int mode) {
        return Arrays.stream(BatteryStoreModeEnum.values()).filter(modeEnum -> modeEnum.mode == mode).findAny()
                .orElseThrow(() -> new CloudSDKException(BatteryStoreModeEnum.class, mode));
    }
}
