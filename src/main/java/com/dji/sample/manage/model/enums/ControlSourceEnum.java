package com.dji.sample.manage.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/16
 */
public enum ControlSourceEnum {

    A, B;

    @JsonValue
    public String getControlSource() {
        return name();
    }

    @JsonCreator
    public static ControlSourceEnum find(String controlSource) {
        return Arrays.stream(values()).filter(controlSourceEnum -> controlSourceEnum.name().equals(controlSource)).findAny().get();
    }
}
