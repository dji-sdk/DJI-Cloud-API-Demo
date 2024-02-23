package com.dji.sdk.cloudapi.property;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @date 2023/12/12
 * @version 1.9
 */
public enum SilentModeEnum {

    RING(0),

    SILENT(1),

    ;

    private final int mode;

    SilentModeEnum(int mode) {
        this.mode = mode;
    }

    @JsonValue
    public int getMode() {
        return mode;
    }

    @JsonCreator
    public static SilentModeEnum find(int mode) {
        return Arrays.stream(values()).filter(modeEnum -> modeEnum.mode == mode).findAny()
            .orElseThrow(() -> new CloudSDKException(SilentModeEnum.class, mode));
    }

}
