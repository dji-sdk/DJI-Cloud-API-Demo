package com.dji.sample.manage.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/9
 */
public enum DeviceModeCodeEnum {

    IDLE(0),

    TAKEOFF_PREPARE(1),

    TAKEOFF_FINISHED(2),

    MANUAL(3),

    TAKEOFF_AUTO(4),

    WAYLINE(5),

    PANORAMIC_SHOT(6),

    ACTIVE_TRACK(7),

    ADS_B_AVOIDANCE(8),

    RETURN_AUTO(9),

    LANDING_AUTO(10),

    LANDING_FORCED(11),

    LANDING_THREE_PROPELLER(12),

    UPGRADING(13),

    DISCONNECTED(14),

    APAS(15),

    VIRTUAL_JOYSTICK(16);

    int val;

    DeviceModeCodeEnum(int val) {
        this.val = val;
    }

    @JsonValue
    public int getVal() {
        return val;
    }

    @JsonCreator
    public static DeviceModeCodeEnum find(int value) {
        return Arrays.stream(values()).filter(modeCodeEnum -> modeCodeEnum.ordinal() == value).findAny().orElse(DISCONNECTED);
    }
}
