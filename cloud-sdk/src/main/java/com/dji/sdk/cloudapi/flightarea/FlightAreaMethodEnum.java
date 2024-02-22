package com.dji.sdk.cloudapi.flightarea;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/17
 */
public enum FlightAreaMethodEnum {

    FLIGHT_AREAS_UPDATE("flight_areas_update"),

    FLIGHT_AREAS_DELETE("flight_areas_delete"),
    ;

    private final String method;

    FlightAreaMethodEnum(String method) {
        this.method = method;
    }

    @JsonValue
    public String getMethod() {
        return method;
    }
}
