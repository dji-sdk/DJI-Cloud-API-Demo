package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/8/4
 */
public enum RthModeEnum {

    OPTIMAL_HEIGHT(0),

    PRESET_HEIGHT(1);

    private final int rthMode;

    RthModeEnum(int rthMode) {
        this.rthMode = rthMode;
    }

    @JsonValue
    public int getRthMode() {
        return rthMode;
    }

    @JsonCreator
    public static RthModeEnum find(int rthMode) {
        return Arrays.stream(values()).filter(rthModeEnum -> rthModeEnum.rthMode == rthMode).findAny()
            .orElseThrow(() -> new CloudSDKException(RthModeEnum.class, rthMode));
    }

}
