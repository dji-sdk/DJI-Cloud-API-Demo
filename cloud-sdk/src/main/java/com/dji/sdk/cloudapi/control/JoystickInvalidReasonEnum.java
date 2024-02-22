package com.dji.sdk.cloudapi.control;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/14
 */
public enum JoystickInvalidReasonEnum {

    RC_LOST(0, "The remote controller is lost."),

    BATTERY_LOW_GO_HOME(1, "Due to low battery, the drone automatically returned home."),

    BATTERY_SUPER_LOW_LANDING(2, "Due to the serious low battery, the drone landed automatically."),

    NEAR_BOUNDARY(3, "The drone is near a not-fly zone."),

    RC_AUTHORITY(4, "The remote controller grabs control authority.");

    private final int reason;

    private final String message;

    JoystickInvalidReasonEnum(int reason, String message) {
        this.reason = reason;
        this.message = message;
    }

    @JsonValue
    public int getVal() {
        return reason;
    }

    public String getMessage() {
        return message;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static JoystickInvalidReasonEnum find(int reason) {
        return Arrays.stream(values()).filter(reasonEnum -> reasonEnum.reason == reason).findAny()
                .orElseThrow(() -> new CloudSDKException(JoystickInvalidReasonEnum.class, reason));
    }
}
