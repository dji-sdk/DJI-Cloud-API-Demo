package com.dji.sdk.cloudapi.property;

import com.dji.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/3
 */
public class RthAltitudeSet extends BaseModel {

    @NotNull
    @Min(20)
    @Max(50)
    @JsonProperty("rth_altitude")
    private Integer rthAltitude;

    public RthAltitudeSet() {
    }

    @Override
    public String toString() {
        return "RthAltitudeSet{" +
                "rthAltitude=" + rthAltitude +
                '}';
    }

    public Integer getRthAltitude() {
        return rthAltitude;
    }

    public RthAltitudeSet setRthAltitude(Integer rthAltitude) {
        this.rthAltitude = rthAltitude;
        return this;
    }
}
