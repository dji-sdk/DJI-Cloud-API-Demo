package com.dji.sdk.cloudapi.device;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 0.2
 * @date 2021/12/8
 */
public class DeviceOsdHost {

    @Schema(description = "device latitude")
    @NotNull
    private Float latitude;

    @Schema(description = "device longitude")
    @NotNull
    private Float longitude;

    @Schema(description = "device ellipsoid height")
    @NotNull
    private Float height;

    @Schema(description = "device head facing angle")
    @NotNull
    @JsonProperty("attitude_head")
    private Float attitudeHead;

    @Schema(description = "height relative to the takeoff point")
    @NotNull
    private Float elevation;

    @Schema(description = "horizontal speed")
    @NotNull
    @JsonProperty("horizontal_speed")
    private Float horizontalSpeed;

    @Schema(description = "vertical speed")
    @NotNull
    @JsonProperty("vertical_speed")
    private Float verticalSpeed;

    public DeviceOsdHost() {
    }

    @Override
    public String toString() {
        return "DeviceOsdHost{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", attitudeHead=" + attitudeHead +
                ", elevation=" + elevation +
                ", horizontalSpeed=" + horizontalSpeed +
                ", verticalSpeed=" + verticalSpeed +
                '}';
    }

    public Float getLatitude() {
        return latitude;
    }

    public DeviceOsdHost setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public DeviceOsdHost setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public Float getHeight() {
        return height;
    }

    public DeviceOsdHost setHeight(Float height) {
        this.height = height;
        return this;
    }

    public Float getAttitudeHead() {
        return attitudeHead;
    }

    public DeviceOsdHost setAttitudeHead(Float attitudeHead) {
        this.attitudeHead = attitudeHead;
        return this;
    }

    public Float getElevation() {
        return elevation;
    }

    public DeviceOsdHost setElevation(Float elevation) {
        this.elevation = elevation;
        return this;
    }

    public Float getHorizontalSpeed() {
        return horizontalSpeed;
    }

    public DeviceOsdHost setHorizontalSpeed(Float horizontalSpeed) {
        this.horizontalSpeed = horizontalSpeed;
        return this;
    }

    public Float getVerticalSpeed() {
        return verticalSpeed;
    }

    public DeviceOsdHost setVerticalSpeed(Float verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
        return this;
    }
}
