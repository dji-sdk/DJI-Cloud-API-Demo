package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/9
 */
public enum DroneModeCodeEnum {

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

    VIRTUAL_JOYSTICK(16),

    LIVE_FLIGHT_CONTROLS(17),

    AERIAL_RTK_FIXED(18),

    DOCK_SITE_EVALUATION(19),

    POI(20),

    ;

    private final int code;

    DroneModeCodeEnum(int code) {
        this.code = code;
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    @JsonCreator
    public static DroneModeCodeEnum find(int code) {
        return Arrays.stream(values()).filter(modeCodeEnum -> modeCodeEnum.ordinal() == code).findAny()
                .orElseThrow(() -> new CloudSDKException(DroneModeCodeEnum.class, code));
    }
}
