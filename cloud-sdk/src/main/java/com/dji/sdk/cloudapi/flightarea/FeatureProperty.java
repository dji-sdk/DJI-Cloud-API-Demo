package com.dji.sdk.cloudapi.flightarea;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/21
 */
public class FeatureProperty {

    @JsonProperty("subType")
    private GeometrySubTypeEnum subType;

    @Min(10)
    private Float radius = 0f;

    private Boolean enable;

    public FeatureProperty() {
    }

    @Override
    public String toString() {
        return "FeatureProperty{" +
                "subType=" + subType +
                ", radius=" + radius +
                ", enable=" + enable +
                '}';
    }

    public GeometrySubTypeEnum getSubType() {
        return subType;
    }

    public FeatureProperty setSubType(GeometrySubTypeEnum subType) {
        this.subType = subType;
        return this;
    }

    public Float getRadius() {
        return radius;
    }

    public FeatureProperty setRadius(Float radius) {
        this.radius = radius;
        return this;
    }

    public Boolean getEnable() {
        return enable;
    }

    public FeatureProperty setEnable(Boolean enable) {
        this.enable = enable;
        return this;
    }
}
