package com.dji.sdk.cloudapi.flightarea;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/21
 */
public class FlightAreaFeature {

    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    @NotNull
    private String id;

    private final String type = "Feature";

    @NotNull
    private GeofenceTypeEnum geofenceType;

    @NotNull
    @Valid
    private FlightAreaGeometry geometry;

    @NotNull
    private FeatureProperty properties;

    public FlightAreaFeature() {
    }

    @Override
    public String toString() {
        return "FlightAreaFeature{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", geofenceType=" + geofenceType +
                ", geometry=" + geometry +
                ", properties=" + properties +
                '}';
    }

    public String getId() {
        return id;
    }

    public FlightAreaFeature setId(String id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public GeofenceTypeEnum getGeofenceType() {
        return geofenceType;
    }

    public FlightAreaFeature setGeofenceType(GeofenceTypeEnum geofenceType) {
        this.geofenceType = geofenceType;
        return this;
    }

    public FlightAreaGeometry getGeometry() {
        return geometry;
    }

    public FlightAreaFeature setGeometry(FlightAreaGeometry geometry) {
        this.geometry = geometry;
        return this;
    }

    public FeatureProperty getProperties() {
        return properties;
    }

    public FlightAreaFeature setProperties(FeatureProperty properties) {
        this.properties = properties;
        return this;
    }
}
