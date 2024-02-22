package com.dji.sdk.cloudapi.map;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
@Schema(description = "The coordinates of the element, the coordinate system is WGS84")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElementCoordinate {

    @Schema(description = "longitude")
    @NotNull
    private Double longitude;

    @Schema(description = "latitude")
    @NotNull
    private Double latitude;

    @Schema(description = "altitude")
    private Double altitude;

    public ElementCoordinate() {
    }

    @Override
    public String toString() {
        return "ElementCoordinate{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                '}';
    }

    public Double getLongitude() {
        return longitude;
    }

    public ElementCoordinate setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public ElementCoordinate setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getAltitude() {
        return altitude;
    }

    public ElementCoordinate setAltitude(Double altitude) {
        this.altitude = altitude;
        return this;
    }
}
