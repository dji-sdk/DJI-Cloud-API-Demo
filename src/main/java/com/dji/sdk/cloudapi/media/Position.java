package com.dji.sdk.cloudapi.media;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/7
 */
public class Position {

    @Schema(description = "latitude", example = "22.577666000000001")
    @NotNull
    private Double lat;

    @Schema(description = "longitude", example = "113.9431940000000")
    @NotNull
    private Double lng;

    public Position() {
    }

    @Override
    public String toString() {
        return "Position{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    public Double getLat() {
        return lat;
    }

    public Position setLat(Double lat) {
        this.lat = lat;
        return this;
    }

    public Double getLng() {
        return lng;
    }

    public Position setLng(Double lng) {
        this.lng = lng;
        return this;
    }
}
