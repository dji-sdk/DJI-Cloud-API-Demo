package com.dji.sdk.cloudapi.map;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
@Schema(enumAsRef = true, description = "<p>0: Point <p/><p>1: LineString <p/><p>2: Polygon</p>", allowableValues = {"0", "1", "2"})
public enum ElementResourceTypeEnum {

    POINT(0, "Point"),

    LINE_STRING(1, "LineString"),

    POLYGON(2, "Polygon");

    private final int type;

    private final String typeName;

    ElementResourceTypeEnum(int type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    @JsonValue
    public int getType() {
        return type;
    }

    @JsonCreator
    public static ElementResourceTypeEnum find(int type) {
        return Arrays.stream(values()).filter(typeEnum -> typeEnum.type == type).findAny()
            .orElseThrow(() -> new CloudSDKException(ElementResourceTypeEnum.class, type));
    }

    public static ElementResourceTypeEnum find(String typeName) {
        return Arrays.stream(values()).filter(typeEnum -> typeEnum.typeName.equals(typeName)).findAny()
            .orElseThrow(() -> new CloudSDKException(ElementResourceTypeEnum.class, typeName));
    }

}
