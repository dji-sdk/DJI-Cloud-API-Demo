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
public enum BatteryIndexEnum {

    LEFT(0),

    RIGHT(1);

    private final int index;

    BatteryIndexEnum(int index) {
        this.index = index;
    }

    @JsonValue
    public int getIndex() {
        return index;
    }

    @JsonCreator
    public static BatteryIndexEnum find(int index) {
        return Arrays.stream(values()).filter(indexEnum -> indexEnum.index == index).findAny()
            .orElseThrow(() -> new CloudSDKException(BatteryIndexEnum.class, index));
    }

}
