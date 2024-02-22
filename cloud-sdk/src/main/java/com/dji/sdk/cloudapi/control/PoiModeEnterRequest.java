package com.dji.sdk.cloudapi.control;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.9
 * @date 2023/12/12
 */
public class PoiModeEnterRequest extends BaseModel {

    @Min(-90)
    @Max(90)
    @NotNull
    private Float latitude;

    @NotNull
    @Min(-180)
    @Max(180)
    private Float longitude;

    @NotNull
    @Min(2)
    @Max(10000)
    private Float height;

    public PoiModeEnterRequest() {
    }

    @Override
    public String toString() {
        return "PoiModeEnterRequest{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                '}';
    }

    public Float getLatitude() {
        return latitude;
    }

    public PoiModeEnterRequest setLatitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public Float getLongitude() {
        return longitude;
    }

    public PoiModeEnterRequest setLongitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public Float getHeight() {
        return height;
    }

    public PoiModeEnterRequest setHeight(Float height) {
        this.height = height;
        return this;
    }
}
