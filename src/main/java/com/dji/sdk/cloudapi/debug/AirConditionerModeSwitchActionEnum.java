package com.dji.sdk.cloudapi.debug;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/28
 */
public enum AirConditionerModeSwitchActionEnum {

    IDLE_MODE(0),

    COOLING_MODE(1),

    heating_mode(2),

    DEHUMIDIFICATION_MODE(3);

    private final int action;

    AirConditionerModeSwitchActionEnum(int action) {
        this.action = action;
    }

    @JsonValue
    public int getAction() {
        return action;
    }

    @JsonCreator
    public static AirConditionerModeSwitchActionEnum find(int action) {
        return Arrays.stream(values()).filter(actionEnum -> actionEnum.action == action).findAny()
                .orElseThrow(() -> new CloudSDKException(AirConditionerModeSwitchActionEnum.class, action));
    }
}
