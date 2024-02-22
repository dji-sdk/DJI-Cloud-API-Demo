package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public enum ExitingRTHReasonEnum {

    ADD_JOYSTICK_THROTTLE(0, "Add joystick throttle"),

    ADD_JOYSTICK_PITCH(1, "Add joystick pitch"),

    INITIALIZATION_FAILED(2, "The initialization of behavior tree is failed"),

    SURROUNDED_BY_OBSTACLES(3, "Surrounded by obstacles"),

    FLIGHT_RESTRICTION(4, "Flight restriction is triggered"),

    OBSTACLE_IS_TOO_CLOSED(5, "Obstacle is too closed"),

    NO_GPS(6, "No GPS signal"),

    GPS_AND_VIO_ARE_FALSE(7, "The output flag of GPS and VIO location is false"),

    ERROR_OF_GPS_AND_VIO(8, "The error of GPS and VIO fusion position is too large"),

    SHORT_DISTANCE_BACKTRACKING(9, "Backtrack in a short distance"),

    TRIGGER_RTH(10, "Trigger the RTH in a short distanc");

    private final int reason;

    private final String msg;

    ExitingRTHReasonEnum(int reason, String msg) {
        this.reason = reason;
        this.msg = msg;
    }

    @JsonValue
    public int getReason() {
        return reason;
    }

    public String getMsg() {
        return msg;
    }

    @JsonCreator
    public static ExitingRTHReasonEnum find(int reason) {
        return Arrays.stream(values()).filter(reasonEnum -> reasonEnum.reason == reason).findAny()
                .orElseThrow(() -> new CloudSDKException(ExitingRTHReasonEnum.class, reason));
    }
}
