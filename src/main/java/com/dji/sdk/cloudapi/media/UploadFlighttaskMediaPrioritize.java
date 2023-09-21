package com.dji.sdk.cloudapi.media;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/27
 */
public class UploadFlighttaskMediaPrioritize extends BaseModel {

    @NotNull
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    private String flightId;

    public UploadFlighttaskMediaPrioritize() {
    }

    @Override
    public String toString() {
        return "UploadFlighttaskMediaPrioritize{" +
                "flightId='" + flightId + '\'' +
                '}';
    }

    public String getFlightId() {
        return flightId;
    }

    public UploadFlighttaskMediaPrioritize setFlightId(String flightId) {
        this.flightId = flightId;
        return this;
    }
}
