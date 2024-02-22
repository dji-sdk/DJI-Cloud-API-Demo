package com.dji.sdk.mqtt.property;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/28
 */
public enum PropertySetReplyResultEnum {

    SUCCESS(0),

    FAILED(1),

    TIMEOUT(2),

    UNKNOWN(-1);

    private final int result;

    PropertySetReplyResultEnum(int result) {
        this.result = result;
    }

    @JsonValue
    public int getResult() {
        return result;
    }

    @JsonCreator
    public static PropertySetReplyResultEnum find(int result) {
        return Arrays.stream(values()).filter(resultEnum -> resultEnum.result == result).findAny().orElse(UNKNOWN);
    }
}
