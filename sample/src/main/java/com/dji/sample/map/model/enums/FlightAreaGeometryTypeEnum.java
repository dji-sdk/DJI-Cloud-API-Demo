package com.dji.sample.map.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.9
 * @date 2023/11/24
 */
public enum FlightAreaGeometryTypeEnum {
    
    CIRCLE("Circle"),

    POLYGON("Polygon"),

    ;

    private final String type;

    FlightAreaGeometryTypeEnum(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator
    public static FlightAreaGeometryTypeEnum find(String type) {
        return Arrays.stream(values()).filter(typeEnum -> typeEnum.type.equals(type)).findAny()
                .orElseThrow(() -> new RuntimeException("This type(" + type + ") is not supported."));
    }
}
