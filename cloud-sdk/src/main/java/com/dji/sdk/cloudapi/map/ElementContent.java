package com.dji.sdk.cloudapi.map;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
@Schema(description = "element content")
public class ElementContent {

    @Schema(defaultValue = "Feature", allowableValues = "Feature")
    @NotNull
    private final String type = "Feature";

    @NotNull
    @Valid
    private ElementProperty properties;

    @Valid
    @NotNull
    private ElementGeometryType geometry;

    public ElementContent() {
    }

    @Override
    public String toString() {
        return "ElementContent{" +
                "type='" + type + '\'' +
                ", properties=" + properties +
                ", geometry=" + geometry +
                '}';
    }

    public String getType() {
        return type;
    }

    public ElementProperty getProperties() {
        return properties;
    }

    public ElementContent setProperties(ElementProperty properties) {
        this.properties = properties;
        return this;
    }

    public ElementGeometryType getGeometry() {
        return geometry;
    }

    public ElementContent setGeometry(ElementGeometryType geometry) {
        this.geometry = geometry;
        return this;
    }
}
