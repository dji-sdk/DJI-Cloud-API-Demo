package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2022/11/3
 */
public enum MaintenanceStateEnum {

    NO_NEED_TO_MAINTENANCE(0),

    NEED_MAINTENANCE(1),

    UNDER_MAINTENANCE(2),

    ;

    private final int state;

    MaintenanceStateEnum(int state) {
        this.state = state;
    }

    @JsonValue
    public int getState() {
        return state;
    }

    @JsonCreator
    public static MaintenanceStateEnum find(int state) {
        return Arrays.stream(values()).filter(stateEnum -> stateEnum.state == state).findAny()
            .orElseThrow(() -> new CloudSDKException(MaintenanceStateEnum.class, state));
    }

}
