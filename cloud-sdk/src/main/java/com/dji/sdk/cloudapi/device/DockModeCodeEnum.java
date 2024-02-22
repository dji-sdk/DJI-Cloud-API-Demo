package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/2/28
 */
public enum DockModeCodeEnum {

    IDLE(0),

    DEBUGGING(1),

    REMOTE_DEBUGGING(2),

    UPGRADING(3),

    WORKING(4);

    private final int code;

    DockModeCodeEnum(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static DockModeCodeEnum find(int code) {
        return Arrays.stream(values()).filter(modeCode -> modeCode.code == code).findAny()
                .orElseThrow(() -> new CloudSDKException(DockModeCodeEnum.class, code));
    }
}
