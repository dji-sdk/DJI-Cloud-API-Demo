package com.dji.sdk.cloudapi.device;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/11
 */
public class AlternateLandPoint {

    private Float latitude;

    private Float longitude;

    private Float safeLandHeight;

    @JsonProperty("is_configured")
    private Boolean configured;

    public AlternateLandPoint() {
    }

    @Override
    public String toString() {
        return "AlternateLandPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", safeLandHeight=" + safeLandHeight +
                ", configured=" + configured +
                '}';
    }

    public Float getLatitude() {
        return latitude;
    }

    public AlternateLandPoint setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public AlternateLandPoint setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public Float getSafeLandHeight() {
        return safeLandHeight;
    }

    public AlternateLandPoint setSafeLandHeight(Float safeLandHeight) {
        this.safeLandHeight = safeLandHeight;
        return this;
    }

    public Boolean getConfigured() {
        return configured;
    }

    public AlternateLandPoint setConfigured(Boolean configured) {
        this.configured = configured;
        return this;
    }
}
