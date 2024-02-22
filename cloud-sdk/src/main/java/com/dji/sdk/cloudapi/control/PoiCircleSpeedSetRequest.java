package com.dji.sdk.cloudapi.control;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class PoiCircleSpeedSetRequest extends BaseModel {

    @NotNull
    private Float circleSpeed;

    public PoiCircleSpeedSetRequest() {
    }

    @Override
    public String toString() {
        return "PoiCircleSpeedSetRequest{" +
                "circleSpeed=" + circleSpeed +
                '}';
    }

    public Float getCircleSpeed() {
        return circleSpeed;
    }

    public PoiCircleSpeedSetRequest setCircleSpeed(Float circleSpeed) {
        this.circleSpeed = circleSpeed;
        return this;
    }
}
