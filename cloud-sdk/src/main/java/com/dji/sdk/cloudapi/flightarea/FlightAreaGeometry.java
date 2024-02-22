package com.dji.sdk.cloudapi.flightarea;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/21
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",
        include = JsonTypeInfo.As.EXISTING_PROPERTY, defaultImpl = FlightAreaGeometry.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FlightAreaPointGeometry.class, name = "Point"),
        @JsonSubTypes.Type(value = FlightAreaPolygonGeometry.class, name = "Polygon")
})
public abstract class FlightAreaGeometry {

    private GeometryTypeEnum type;

}
