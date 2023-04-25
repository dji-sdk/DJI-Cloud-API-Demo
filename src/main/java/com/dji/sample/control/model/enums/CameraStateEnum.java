package com.dji.sample.control.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/4/23
 */
public enum CameraStateEnum {

    IDLE, WORKING;

    @JsonValue
    public int getVal() {
        return ordinal();
    }

    @JsonCreator
    public static CameraStateEnum find(int val) {
        return Arrays.stream(values()).filter(stateEnum -> stateEnum.ordinal() == val).findAny().get();
    }
}
