package com.dji.sample.control.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/14
 */
public enum DrcModeReasonEnum {

    UNKNOWN(-1, "unknown"),

    RC_LOST(0, "The remote controller is lost."),

    BATTERY_LOW_GO_HOME(1, "Due to low battery, the drone automatically returned home."),

    BATTERY_SUPER_LOW_LANDING(2, "Due to the serious low battery, the drone landed automatically."),

    NEAR_BOUNDARY(3, "The drone is near a not-fly zone."),

    RC_AUTHORITY(4, "The remote controller grabs control authority.");

    int val;

    String message;

    DrcModeReasonEnum(int val, String message) {
        this.val = val;
        this.message = message;
    }

    public int getVal() {
        return val;
    }

    public String getMessage() {
        return message;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static DrcModeReasonEnum find(int val) {
        return Arrays.stream(values()).filter(reasonEnum -> reasonEnum.val == val).findAny().orElse(UNKNOWN);
    }
}
