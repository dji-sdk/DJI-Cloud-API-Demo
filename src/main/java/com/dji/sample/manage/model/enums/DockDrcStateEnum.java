package com.dji.sample.manage.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/2/28
 */
public enum DockDrcStateEnum {

    DISCONNECTED(0),

    CONNECTING(1),

    CONNECTED(2);

    int val;

    DockDrcStateEnum(int val) {
        this.val = val;
    }

    @JsonValue
    public int getVal() {
        return val;
    }

    @JsonCreator
    public static DockDrcStateEnum find(int val) {
        return Arrays.stream(values()).filter(drcState -> drcState.getVal() == val).findAny().orElse(DISCONNECTED);
    }
}
