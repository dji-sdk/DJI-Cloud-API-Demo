package com.dji.sdk.cloudapi.flightarea;

import java.util.List;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/17
 */
public class FlightAreasDroneLocation {

    /**
     * Flight area distance to aircraft
     */
    private List<DroneLocation> droneLocations;

    public FlightAreasDroneLocation() {
    }

    @Override
    public String toString() {
        return "FlightAreasDroneLocation{" +
                "droneLocations=" + droneLocations +
                '}';
    }

    public List<DroneLocation> getDroneLocations() {
        return droneLocations;
    }

    public FlightAreasDroneLocation setDroneLocations(List<DroneLocation> droneLocations) {
        this.droneLocations = droneLocations;
        return this;
    }
}
