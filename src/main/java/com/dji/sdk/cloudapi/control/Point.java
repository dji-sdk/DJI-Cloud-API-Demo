package com.dji.sdk.cloudapi.control;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.3
 * @date 2023/2/14
 */
public class Point {

    @Min(-90)
    @Max(90)
    @NotNull
    private Float latitude;

    @NotNull
    @Min(-180)
    @Max(180)
    private Float longitude;

    /**
     * WGS84
     * The M30 series are ellipsoidal heights.
     */
    @NotNull
    @Min(2)
    @Max(10000)
    private Float height;

    public Point() {
    }

    @Override
    public String toString() {
        return "Point{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                '}';
    }

    public Float getLatitude() {
        return latitude;
    }

    public Point setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Point setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public Float getHeight() {
        return height;
    }

    public Point setHeight(Float height) {
        this.height = height;
        return this;
    }
}
