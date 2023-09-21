package com.dji.sdk.cloudapi.wayline;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/25
 */
public class FlighttaskResourceGetRequest {

    private String flightId;

    public FlighttaskResourceGetRequest() {
    }

    @Override
    public String toString() {
        return "FlighttaskResourceGetRequest{" +
                "flightId='" + flightId + '\'' +
                '}';
    }

    public String getFlightId() {
        return flightId;
    }

    public FlighttaskResourceGetRequest setFlightId(String flightId) {
        this.flightId = flightId;
        return this;
    }
}
