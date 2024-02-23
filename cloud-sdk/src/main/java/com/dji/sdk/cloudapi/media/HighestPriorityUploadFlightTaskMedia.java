package com.dji.sdk.cloudapi.media;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/27
 */
public class HighestPriorityUploadFlightTaskMedia {

    private String flightId;

    public HighestPriorityUploadFlightTaskMedia() {
    }

    @Override
    public String toString() {
        return "HighestPriorityUploadFlightTaskMedia{" +
                "flightId='" + flightId + '\'' +
                '}';
    }

    public String getFlightId() {
        return flightId;
    }

    public HighestPriorityUploadFlightTaskMedia setFlightId(String flightId) {
        this.flightId = flightId;
        return this;
    }
}
