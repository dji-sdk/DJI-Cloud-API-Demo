package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public enum PositionFixedEnum {

    NOT_START(0),

    FIXING(1),

    SUCCESSFUL(2),

    FAILED(3),
    ;

    private final int fixed;

    PositionFixedEnum(int fixed) {
        this.fixed = fixed;
    }

    @JsonValue
    public int getFixed() {
        return fixed;
    }

    @JsonCreator
    public static PositionFixedEnum find(int fixed) {
        return Arrays.stream(values()).filter(fixedEnum -> fixedEnum.fixed == fixed).findAny()
            .orElseThrow(() -> new CloudSDKException(PositionFixedEnum.class, fixed));
    }

}
