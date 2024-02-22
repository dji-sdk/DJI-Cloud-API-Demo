package com.dji.sdk.cloudapi.flightarea;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/17
 */
public class DroneLocation {

    /**
     * Region unique ID
     */
    private String areaId;

    /**
     * Distance to the custom flight area boundary
     */
    private Float areaDistance;

    /**
     * Whether in custom flight area
     */
    @JsonProperty("is_in_area")
    private Boolean inArea;

    public String getAreaId() {
        return areaId;
    }

    public DroneLocation setAreaId(String areaId) {
        this.areaId = areaId;
        return this;
    }

    public Float getAreaDistance() {
        return areaDistance;
    }

    public DroneLocation setAreaDistance(Float areaDistance) {
        this.areaDistance = areaDistance;
        return this;
    }

    public Boolean getInArea() {
        return inArea;
    }

    public DroneLocation setInArea(Boolean inArea) {
        this.inArea = inArea;
        return this;
    }
}
