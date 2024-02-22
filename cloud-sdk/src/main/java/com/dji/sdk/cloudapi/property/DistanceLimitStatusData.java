package com.dji.sdk.cloudapi.property;

import com.dji.sdk.cloudapi.device.SwitchActionEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * The state of the drone's limited distance
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
public class DistanceLimitStatusData {

    private SwitchActionEnum state;

    @Min(15)
    @Max(8000)
    @JsonProperty("distance_limit")
    private Integer distanceLimit;

    public DistanceLimitStatusData() {
    }

    @Override
    public String toString() {
        return "DistanceLimitStatusData{" +
                "state=" + state +
                ", distanceLimit=" + distanceLimit +
                '}';
    }

    public SwitchActionEnum getState() {
        return state;
    }

    public DistanceLimitStatusData setState(SwitchActionEnum state) {
        this.state = state;
        return this;
    }

    public Integer getDistanceLimit() {
        return distanceLimit;
    }

    public DistanceLimitStatusData setDistanceLimit(Integer distanceLimit) {
        this.distanceLimit = distanceLimit;
        return this;
    }
}
