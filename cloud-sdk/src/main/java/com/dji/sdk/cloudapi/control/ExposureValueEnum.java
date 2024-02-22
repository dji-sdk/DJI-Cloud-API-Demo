package com.dji.sdk.cloudapi.control;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public enum ExposureValueEnum {

    MINUS_5_DOT_0(1, "-5.0EV"),

    MINUS_4_DOT_7(2, "-4.7EV"),

    MINUS_4_DOT_3(3, "-4.3EV"),

    MINUS_4_DOT_0(4, "-4.0EV"),

    MINUS_3_DOT_7(5, "-3.7EV"),

    MINUS_3_DOT_3(6, "-3.3EV"),

    MINUS_3_DOT_0(7, "-3.0EV"),

    MINUS_2_DOT_7(8, "-2.7EV"),

    MINUS_2_DOT_3(9, "-2.3EV"),

    MINUS_2_DOT_0(10, "-2.0EV"),

    MINUS_1_DOT_7(11, "-1.7EV"),

    MINUS_1_DOT_3(12, "-1.3EV"),

    MINUS_1_DOT_0(13, "-1.0EV"),

    MINUS_0_DOT_7(14, "-0.7EV"),

    MINUS_0_DOT_3(15, "-0.3EV"),

    _0(16, "0EV"),

    _0_DOT_3(17, "0.3EV"),

    _0_DOT_7(18, "0.7EV"),

    _1_DOT_0(19, "1.0EV"),

    _1_DOT_3(20, "1.3EV"),

    _1_DOT_7(21, "1.7EV"),

    _2_DOT_0(22, "2.0EV"),

    _2_DOT_3(23, "2.3EV"),

    _2_DOT_7(24, "2.7EV"),

    _3_DOT_0(25, "3.0EV"),

    _3_DOT_3(26, "3.3EV"),

    _3_DOT_7(27, "3.7EV"),

    _4_DOT_0(28, "4.0EV"),

    _4_DOT_3(29, "4.3EV"),

    _4_DOT_7(30, "4.7EV"),

    _5_DOT_0(31, "5.0EV"),

    FIXED(255, "FIXED"),

    ;


    private final int value;

    private final String desc;

    ExposureValueEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    @JsonCreator
    public static ExposureValueEnum find(int value) {
        return Arrays.stream(values()).filter(valueEnum -> valueEnum.value == value).findAny()
            .orElseThrow(() -> new CloudSDKException(ExposureValueEnum.class, value));
    }

}
