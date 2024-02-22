package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/11
 */
public enum WaylineMissionStateEnum {

    DISCONNECT(0, "Disconnect"),

    NOT_SUPPORTED_WAYPOINT(1, "Do not support this waypoint"),

    WAYLINE_PREPARING(2, "Wayline is ready. File can be uploaded and uploaded file can be executed."),

    WAYLINE_UPLOADING(3, "Wayline file is uploading"),

    DRONE_PREPARING(4, "Trigger start command. Trgger aircraft reading wayline. Not start. Under preparation."),

    ARRIVE_FIRST_WAYPOINT(5, "Enter wayline and arrive first waypoint"),

    WAYLINE_EXECUTING(6, "Execute wayline"),

    WAYLINE_BROKEN(7, "Wayline is broken. Trigger reason: 1. User pauses the wayline. 2. Flight control is abnormal."),

    WAYLINE_RECOVER(8, "Wayline recover"),

    WAYLINE_END(9, "Wayline stop"),

    ;

    private final int state;

    private final String msg;

    WaylineMissionStateEnum(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    @JsonValue
    public int getState() {
        return state;
    }

    public String getMsg() {
        return msg;
    }

    @JsonCreator
    public static WaylineMissionStateEnum find(int state) {
        return Arrays.stream(values()).filter(stateEnum -> stateEnum.state == state).findAny()
            .orElseThrow(() -> new CloudSDKException(WaylineMissionStateEnum.class, state));
    }

}
