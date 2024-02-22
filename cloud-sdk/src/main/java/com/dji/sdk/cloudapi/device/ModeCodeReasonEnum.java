package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/2/28
 */
public enum ModeCodeReasonEnum {

    NO_MEANING(0),

    LOW_POWER(1),

    LOW_VOLTAGE(2),

    SERIOUS_LOW_VOLTAGE(3),

    RC_CONTROL(4),

    APP_CONTROL(5),

    RC_SIGNAL_LOST(6),

    EXTERNAL_DEVICE_TRIGGERED(7),

    GEO_ZONE(8),

    HOME_POINT_TOO_CLOSED(9),

    HOME_POINT_TOO_FAR(10),

    EXECUTING_WAYPOINT_MISSION(11),

    ARRIVE_HOME_POINT(12),

    SECOND_LIMIT_LANDING(13),

    APP_FORCIBLY_BREAK_PROTECTION(14),

    PLANES_PASSING_NEARBY(15),

    HEIGHT_CONTROL_FAILED(16),

    LOW_POWER_RTH(17),

    AP_CONTROL(18),

    HARDWARE_ABNORMAL(19),

    TOUCHDOWN_AVOIDANCE_PROTECTION(20),

    CANCEL_RTH(21),

    RTH_OBSTACLE_AVOIDANCE(22),

    RTH_STRONG_GALE(23),

    ;

    private final int reason;

    ModeCodeReasonEnum(int reason) {
        this.reason = reason;
    }

    @JsonValue
    public int getReason() {
        return reason;
    }

    @JsonCreator
    public static ModeCodeReasonEnum find(int reason) {
        return Arrays.stream(values()).filter(reasonEnum -> reasonEnum.reason == reason).findAny()
                .orElseThrow(() -> new CloudSDKException(ModeCodeReasonEnum.class, reason));
    }
}
