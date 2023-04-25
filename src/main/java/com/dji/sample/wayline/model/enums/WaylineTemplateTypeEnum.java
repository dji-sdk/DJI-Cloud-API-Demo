package com.dji.sample.wayline.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author sean
 * @version 1.3
 * @date 2022/9/26
 */
public enum WaylineTemplateTypeEnum {

    WAYPOINT(0, "waypoint"),

    MAPPING_2D(1, "mapping2d"),

    MAPPING_3D(2, "mapping3d"),

    MAPPING_STRIP(4, "mappingStrip");

    int val;

    String type;

    WaylineTemplateTypeEnum(int val, String type) {
        this.val = val;
        this.type = type;
    }

    @JsonValue
    public int getVal() {
        return val;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static WaylineTemplateTypeEnum find(Integer val) {
        return Arrays.stream(values()).filter(templateTypeEnum -> templateTypeEnum.val == val).findAny().get();
    }

    public static Optional<WaylineTemplateTypeEnum> find(String type) {
        return Arrays.stream(values()).filter(templateTypeEnum -> templateTypeEnum.type.equals(type)).findAny();
    }
}
