package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/3
 */
public enum CameraModeEnum {

    PHOTO(0),

    VIDEO(1),

    LOW_LIGHT_INTELLIGENCE(2),

    PANORAMA(3),

    UNSUPPORTED(-1),
    
    ;

    private final int mode;

    CameraModeEnum(int mode) {
        this.mode = mode;
    }

    @JsonValue
    public int getMode() {
        return mode;
    }

    @JsonCreator
    public static CameraModeEnum find(int mode) {
        return Arrays.stream(values()).filter(modeEnum -> modeEnum.mode == mode).findAny()
                .orElseThrow(() -> new CloudSDKException(CameraModeEnum.class, mode));
    }
}
