package com.dji.sample.manage.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/1
 */
public enum DroneRcLostActionEnum {

    HOVER, LAND, RETURN_HOME;

    @JsonValue
    public int getVal() {
        return ordinal();
    }

    @JsonCreator
    public static DroneRcLostActionEnum find(int val) {
        return Arrays.stream(values()).filter(controlActionEnum -> controlActionEnum.ordinal() == val).findAny().get();
    }
}
