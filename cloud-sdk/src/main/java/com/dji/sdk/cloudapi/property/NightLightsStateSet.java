package com.dji.sdk.cloudapi.property;

import com.dji.sdk.cloudapi.device.SwitchActionEnum;
import com.dji.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/25
 */
public class NightLightsStateSet extends BaseModel {

    @NotNull
    @JsonProperty("night_lights_state")
    private SwitchActionEnum nightLightsState;

    public NightLightsStateSet() {
    }

    @Override
    public String toString() {
        return "NightLightsStateSet{" +
                "nightLightsState=" + nightLightsState +
                '}';
    }

    public SwitchActionEnum getNightLightsState() {
        return nightLightsState;
    }

    public NightLightsStateSet setNightLightsState(SwitchActionEnum nightLightsState) {
        this.nightLightsState = nightLightsState;
        return this;
    }
}
