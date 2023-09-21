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
public enum RainfallEnum {

    NO(0),

    LIGHT(1),

    MODERATE(2),

    HEAVY(3),
    ;

    private final int rain;

    RainfallEnum(int rain) {
        this.rain = rain;
    }

    @JsonValue
    public int getRain() {
        return rain;
    }

    @JsonCreator
    public static RainfallEnum find(int rain) {
        return Arrays.stream(values()).filter(rainEnum -> rainEnum.rain == rain).findAny()
            .orElseThrow(() -> new CloudSDKException(RainfallEnum.class, rain));
    }

}
