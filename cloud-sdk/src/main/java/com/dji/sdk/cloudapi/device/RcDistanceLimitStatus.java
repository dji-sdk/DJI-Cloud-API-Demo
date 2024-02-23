package com.dji.sdk.cloudapi.device;

/**
 * The state of the drone's limited distance
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
public class RcDistanceLimitStatus {

    private Integer state;

    private Integer distanceLimit;

    public RcDistanceLimitStatus() {
    }

    @Override
    public String toString() {
        return "RcDistanceLimitStatusSet{" +
                "state=" + state +
                ", distanceLimit=" + distanceLimit +
                '}';
    }

    public Integer getState() {
        return state;
    }

    public RcDistanceLimitStatus setState(Integer state) {
        this.state = state;
        return this;
    }

    public Integer getDistanceLimit() {
        return distanceLimit;
    }

    public RcDistanceLimitStatus setDistanceLimit(Integer distanceLimit) {
        this.distanceLimit = distanceLimit;
        return this;
    }
}
