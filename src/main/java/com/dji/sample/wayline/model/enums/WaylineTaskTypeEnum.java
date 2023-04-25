package com.dji.sample.wayline.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.3
 * @date 2022/9/26
 */
public enum WaylineTaskTypeEnum {

    IMMEDIATE(0),

    TIMED(1),

    CONDITION(2);

    int val;

    WaylineTaskTypeEnum(int val) {
        this.val = val;
    }

    @JsonValue
    public int getVal() {
        return val;
    }

    @JsonCreator
    public static WaylineTaskTypeEnum find(Integer val) {
        return Arrays.stream(values()).filter(taskTypeEnum -> taskTypeEnum.val == val).findAny().get();
    }
}
