package com.dji.sdk.cloudapi.control;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/13
 */
public enum GimbalResetModeEnum {

    RECENTER(0),

    DOWN(1),

    RECENTER_PAN(2),

    PITCH_DOWN(3);

    private final int mode;

    GimbalResetModeEnum(int mode) {
        this.mode = mode;
    }

    @JsonValue
    public int getMode() {
        return mode;
    }

    @JsonCreator
    public static GimbalResetModeEnum find(int mode) {
        return Arrays.stream(values()).filter(resetModeEnum -> resetModeEnum.ordinal() == mode).findAny()
                .orElseThrow(() -> new CloudSDKException(GimbalResetModeEnum.class, mode));
    }
}
