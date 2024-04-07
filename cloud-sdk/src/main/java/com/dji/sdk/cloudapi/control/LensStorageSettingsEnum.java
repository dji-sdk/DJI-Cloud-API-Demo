package com.dji.sdk.cloudapi.control;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/12
 */
public enum LensStorageSettingsEnum {

    CURRENT("current"),

    ZOOM("zoom"),

    WIDE("wide"),

    VISION("vision"),

    INFRARED("ir");

    private final String lens;

    LensStorageSettingsEnum(String lens) {
        this.lens = lens;
    }

    @JsonValue
    public String getLens() {
        return lens;
    }

    @JsonCreator
    public static LensStorageSettingsEnum find(String lens) {
        return Arrays.stream(values()).filter(lensEnum -> lensEnum.lens.equals(lens)).findAny()
            .orElseThrow(() -> new CloudSDKException(LensStorageSettingsEnum.class, lens));
    }
}
