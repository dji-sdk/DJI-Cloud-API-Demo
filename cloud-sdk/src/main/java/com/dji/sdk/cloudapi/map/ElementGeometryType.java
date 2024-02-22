package com.dji.sdk.cloudapi.map;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",
        include = JsonTypeInfo.As.EXISTING_PROPERTY, defaultImpl = ElementGeometryType.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ElementCircleGeometry.class, name = "Circle"),
        @JsonSubTypes.Type(value = ElementPointGeometry.class, name = "Point"),
        @JsonSubTypes.Type(value = ElementLineStringGeometry.class, name = "LineString"),
        @JsonSubTypes.Type(value = ElementPolygonGeometry.class, name = "Polygon")
})
@Schema(oneOf = {ElementPointGeometry.class, ElementLineStringGeometry.class, ElementPolygonGeometry.class})
public abstract class ElementGeometryType {

    private String type;

    ElementGeometryType(String type) {
        this.type = type;
    }

    public ElementGeometryType() {
    }

    public String getType() {
        return type;
    }

    /**
     * Convert coordinate data into objects and then add them to the collection.
     * @return
     */
    public abstract List<ElementCoordinate> convertToList();

    /**
     * Converts coordinates in a collection of objects to a specific type.
     * @param coordinateList
     */
    public abstract void adapterCoordinateType(List<ElementCoordinate> coordinateList);
}
