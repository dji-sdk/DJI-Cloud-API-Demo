package com.dji.sdk.cloudapi.wayline;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.3
 * @date 2022/9/26
 */
@Schema(enumAsRef = true, type = "int", allowableValues = {"0", "1", "2", "3"},
        description = "<p>0: waypoint<p/><p>1: mapping2d<p/><p>2: mapping3d<p/><p>3: mappingStrip</p>")
public enum WaylineTypeEnum {

    WAYPOINT(0, "waypoint"),

    MAPPING_2D(1, "mapping2d"),

    MAPPING_3D(2, "mapping3d"),

    MAPPING_STRIP(3, "mappingStrip");

    private final int value;

    private final String type;

    WaylineTypeEnum(int value, String type) {
        this.value = value;
        this.type = type;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static WaylineTypeEnum find(int value) {
        return Arrays.stream(values()).filter(typeEnum -> typeEnum.value == value).findAny()
                .orElseThrow(() -> new CloudSDKException(WaylineTypeEnum.class, value));
    }

    public static WaylineTypeEnum find(String type) {
        return Arrays.stream(values()).filter(typeEnum -> typeEnum.type.equals(type)).findAny()
                .orElseThrow(() -> new CloudSDKException(WaylineTypeEnum.class, type));
    }
}
