package com.dji.sdk.cloudapi.flightarea;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/21
 */
public class FlightAreaPointGeometry extends FlightAreaGeometry {

    private final GeometryTypeEnum type = GeometryTypeEnum.POINT;

    private Double[] coordinates;

    public FlightAreaPointGeometry() {
    }

    @Override
    public String toString() {
        return "FlightAreaPointGeometry{" +
                "type=" + type +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }

    public GeometryTypeEnum getType() {
        return type;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public FlightAreaPointGeometry setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
        return this;
    }
}
