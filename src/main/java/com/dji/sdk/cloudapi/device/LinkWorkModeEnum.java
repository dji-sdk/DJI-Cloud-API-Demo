package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/25
 */
public enum LinkWorkModeEnum {

    SDR_ONLY(0),

    SDR_WITH_4G(1);

    private final int mode;

    LinkWorkModeEnum(int mode) {
        this.mode = mode;
    }

    @JsonValue
    public int getMode() {
        return mode;
    }

    @JsonCreator
    public static LinkWorkModeEnum find(int mode) {
        return Arrays.stream(LinkWorkModeEnum.values()).filter(modeEnum -> modeEnum.mode == mode).findAny()
                .orElseThrow(() -> new CloudSDKException(LinkWorkModeEnum.class, mode));
    }
}
