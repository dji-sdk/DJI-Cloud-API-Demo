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
public enum VertTrendEnum {

    RELATIVE_HEIGHT_UNCHANGED(0),

    RELATIVE_HEIGHT_INCREASE(1),

    RELATIVE_HEIGHT_DECREASE(2),

    ;

    private final int trend;

    VertTrendEnum(int trend) {
        this.trend = trend;
    }

    @JsonValue
    public int getTrend() {
        return trend;
    }

    @JsonCreator
    public static VertTrendEnum find(int trend) {
        return Arrays.stream(values()).filter(trendEnum -> trendEnum.trend == trend).findAny()
            .orElseThrow(() -> new CloudSDKException(VertTrendEnum.class, trend));
    }

}
