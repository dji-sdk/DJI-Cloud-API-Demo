package com.dji.sdk.cloudapi.flightarea;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/21
 */
public class FlightAreaJson {

    private final String type = "FeatureCollection";

    @NotNull
    private List<FlightAreaFeature> features;

    public FlightAreaJson() {
    }

    @Override
    public String toString() {
        return "FlightAreaJson{" +
                "type='" + type + '\'' +
                ", features=" + features +
                '}';
    }

    public String getType() {
        return type;
    }

    public List<FlightAreaFeature> getFeatures() {
        return features;
    }

    public FlightAreaJson setFeatures(List<FlightAreaFeature> features) {
        this.features = features;
        return this;
    }
}
