package com.dji.sdk.cloudapi.airsense;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/16
 */
public enum WarningLevelEnum {

    ZERO(0),

    ONE(1),

    TWO(2),

    THREE(3),

    FOUR(4),

    ;

    private final int level;

    WarningLevelEnum(int level) {
        this.level = level;
    }

    @JsonValue
    public int getLevel() {
        return level;
    }

    @JsonCreator
    public static WarningLevelEnum find(int level) {
        return Arrays.stream(values()).filter(levelEnum -> levelEnum.level == level).findAny()
            .orElseThrow(() -> new CloudSDKException(WarningLevelEnum.class, level));
    }

}
