package com.dji.sample.manage.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/2/28
 */
public enum DockModeCodeEnum {

    IDLE(0),

    DEBUGGING(1),

    REMOTE_DEBUGGING(2),

    UPGRADING(3),

    WORKING(4),

    DISCONNECTED(-1);

    int val;

    DockModeCodeEnum(int val) {
        this.val = val;
    }

    @JsonValue
    public int getVal() {
        return val;
    }

    @JsonCreator
    public static DockModeCodeEnum find(int val) {
        return Arrays.stream(values()).filter(modeCode -> modeCode.getVal() == val).findAny().orElse(DISCONNECTED);
    }
}
