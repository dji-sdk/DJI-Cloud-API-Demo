package com.dji.sdk.cloudapi.property;

import com.dji.sdk.common.BaseModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * The state of the drone's limited distance
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
public class DistanceLimitStatusSet extends BaseModel {

    @Valid
    @NotNull
    private DistanceLimitStatusData distanceLimitStatus;

    public DistanceLimitStatusSet() {
    }

    @Override
    public String toString() {
        return "DistanceLimitStatusSet{" +
                "distanceLimitStatus=" + distanceLimitStatus +
                '}';
    }

    public DistanceLimitStatusData getDistanceLimitStatus() {
        return distanceLimitStatus;
    }

    public DistanceLimitStatusSet setDistanceLimitStatus(DistanceLimitStatusData distanceLimitStatus) {
        this.distanceLimitStatus = distanceLimitStatus;
        return this;
    }
}
