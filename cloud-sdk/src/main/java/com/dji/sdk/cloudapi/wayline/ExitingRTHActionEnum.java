package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.cloudapi.device.ExitWaylineWhenRcLostEnum;
import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/6
 */
public enum ExitingRTHActionEnum {

    EXIT(0, "Exit exiting RTH state"),

    Enter(1, "Enter exiting RTH state");

    private final int action;

    private final String msg;

    ExitingRTHActionEnum(int action, String msg) {
        this.action = action;
        this.msg = msg;
    }

    @JsonValue
    public int getAction() {
        return action;
    }

    public String getMsg() {
        return msg;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ExitingRTHActionEnum find(int action) {
        return Arrays.stream(values()).filter(actionEnum -> actionEnum.action == action).findAny()
                .orElseThrow(() -> new CloudSDKException(ExitWaylineWhenRcLostEnum.class, action));
    }
}
