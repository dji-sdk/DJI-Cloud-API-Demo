package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/25
 */
public enum PayloadPositionEnum {

    FRONT_LEFT(0),

    FRONT_RIGHT(1),

    TOP(2),

    FPV(7);

    private final int position;

    PayloadPositionEnum(int position) {
        this.position = position;
    }

    @JsonValue
    public int getPosition() {
        return position;
    }

    @JsonCreator
    public static PayloadPositionEnum find(int position) {
        return Arrays.stream(values()).filter(positionEnum -> positionEnum.position == position).findAny()
                .orElseThrow(() -> new CloudSDKException(PayloadPositionEnum.class, position));
    }
}
