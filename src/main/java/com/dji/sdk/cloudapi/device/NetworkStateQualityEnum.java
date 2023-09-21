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
public enum NetworkStateQualityEnum {

    BAD(0),

    MODERATE(1),

    GOOD(2),
    ;

    private final int quality;

    NetworkStateQualityEnum(int quality) {
        this.quality = quality;
    }

    @JsonValue
    public int getQuality() {
        return quality;
    }

    @JsonCreator
    public static NetworkStateQualityEnum find(int quality) {
        return Arrays.stream(values()).filter(qualityEnum -> qualityEnum.quality == quality).findAny()
            .orElseThrow(() -> new CloudSDKException(NetworkStateQualityEnum.class, quality));
    }

}
