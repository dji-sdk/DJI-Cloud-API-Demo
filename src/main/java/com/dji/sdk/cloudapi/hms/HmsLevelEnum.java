package com.dji.sdk.cloudapi.hms;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/27
 */
public enum HmsLevelEnum {

    INFORM(0),

    NOTICE(1),

    ALARM(2);

    private final int level;

    HmsLevelEnum(int level) {
        this.level = level;
    }

    @JsonValue
    public int getLevel() {
        return level;
    }

    @JsonCreator
    public static HmsLevelEnum find(int level) {
        return Arrays.stream(values()).filter(levelEnum -> levelEnum.level == level).findAny()
                .orElseThrow(() -> new CloudSDKException(HmsLevelEnum.class, level));
    }
}
