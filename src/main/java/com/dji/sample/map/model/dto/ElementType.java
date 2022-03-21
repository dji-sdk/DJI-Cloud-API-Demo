package com.dji.sample.map.model.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",
        include = JsonTypeInfo.As.EXISTING_PROPERTY, defaultImpl = ElementType.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ElementPointDTO.class, name = "Point"),
        @JsonSubTypes.Type(value = ElementLineStringDTO.class, name = "LineString"),
        @JsonSubTypes.Type(value = ElementPolygonDTO.class, name = "Polygon")
})
public abstract class ElementType {

    private String type;

    ElementType(String type) {
        this.type = type;
    }

    /**
     * Convert coordinate data into objects and then add them to the collection.
     * @return
     */
    public abstract List<ElementCoordinateDTO> convertToList();

    /**
     * Converts coordinates in a collection of objects to a specific type.
     * @param coordinateList
     */
    public abstract void adapterCoordinateType(List<ElementCoordinateDTO> coordinateList);
}
