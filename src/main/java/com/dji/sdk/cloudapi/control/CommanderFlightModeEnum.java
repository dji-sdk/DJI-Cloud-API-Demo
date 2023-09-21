package com.dji.sdk.cloudapi.control;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/8/7
 */
public enum CommanderFlightModeEnum {

    SMART_HEIGHT(0),

    SETTING_HEIGHT(1);

    private final int mode;

    CommanderFlightModeEnum(int mode) {
        this.mode = mode;
    }

    @JsonValue
    public int getMode() {
        return mode;
    }

    @JsonCreator
    public static CommanderFlightModeEnum find(int mode) {
        return Arrays.stream(values()).filter(modeEnum -> modeEnum.mode == mode).findAny()
            .orElseThrow(() -> new CloudSDKException(CommanderFlightModeEnum.class, mode));
    }

}
