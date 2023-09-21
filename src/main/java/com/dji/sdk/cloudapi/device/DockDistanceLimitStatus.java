package com.dji.sdk.cloudapi.device;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The state of the drone's limited distance
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
public class DockDistanceLimitStatus {

    private SwitchActionEnum state;

    private Integer distanceLimit;

    @JsonProperty("is_near_distance_limit")
    private Boolean nearDistanceLimit;

    public DockDistanceLimitStatus() {
    }

    @Override
    public String toString() {
        return "DockDistanceLimitStatusSet{" +
                "state=" + state +
                ", distanceLimit=" + distanceLimit +
                ", nearDistanceLimit=" + nearDistanceLimit +
                '}';
    }

    public SwitchActionEnum getState() {
        return state;
    }

    public DockDistanceLimitStatus setState(SwitchActionEnum state) {
        this.state = state;
        return this;
    }

    public Integer getDistanceLimit() {
        return distanceLimit;
    }

    public DockDistanceLimitStatus setDistanceLimit(Integer distanceLimit) {
        this.distanceLimit = distanceLimit;
        return this;
    }

    public Boolean getNearDistanceLimit() {
        return nearDistanceLimit;
    }

    public DockDistanceLimitStatus setNearDistanceLimit(Boolean nearDistanceLimit) {
        this.nearDistanceLimit = nearDistanceLimit;
        return this;
    }
}
