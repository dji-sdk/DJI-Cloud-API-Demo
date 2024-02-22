package com.dji.sdk.cloudapi.debug;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/25
 */
public class AirConditionerModeSwitchRequest extends BaseModel {

    @NotNull
    private AirConditionerModeSwitchActionEnum action;

    public AirConditionerModeSwitchRequest() {
    }

    @Override
    public String toString() {
        return "AirConditionerModeSwitchRequest{" +
                "action=" + action +
                '}';
    }

    public AirConditionerModeSwitchActionEnum getAction() {
        return action;
    }

    public AirConditionerModeSwitchRequest setAction(AirConditionerModeSwitchActionEnum action) {
        this.action = action;
        return this;
    }
}
