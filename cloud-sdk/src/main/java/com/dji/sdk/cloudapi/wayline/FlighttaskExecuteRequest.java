package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.common.BaseModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
public class FlighttaskExecuteRequest extends BaseModel {

    @NotNull
    @Pattern(regexp = "^[^<>:\"/|?*._\\\\]+$")
    private String flightId;

    public FlighttaskExecuteRequest() {
    }

    @Override
    public String toString() {
        return "FlighttaskExecuteRequest{" +
                "flightId='" + flightId + '\'' +
                '}';
    }

    public String getFlightId() {
        return flightId;
    }

    public FlighttaskExecuteRequest setFlightId(String flightId) {
        this.flightId = flightId;
        return this;
    }
}
