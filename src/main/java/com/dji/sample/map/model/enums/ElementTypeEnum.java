package com.dji.sample.map.model.enums;

import com.dji.sample.map.model.dto.ElementLineStringDTO;
import com.dji.sample.map.model.dto.ElementPointDTO;
import com.dji.sample.map.model.dto.ElementPolygonDTO;
import com.dji.sample.map.model.dto.ElementType;

import java.util.Optional;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/30
 */
public enum ElementTypeEnum {

    POINT(0, "Point"),

    LINE_STRING(1, "LineString"),

    POLYGON(2, "Polygon"),

    UNKNOWN(-1, "Unknown");

    private int val;

    private String desc;

    ElementTypeEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public static Optional<ElementType> findType(int val) {
        if (POINT.val == val) {
            return Optional.of(new ElementPointDTO());
        }

        if (LINE_STRING.val == val) {
            return Optional.of(new ElementLineStringDTO());
        }

        if (POLYGON.val == val) {
            return Optional.of(new ElementPolygonDTO());
        }

        return Optional.empty();
    }

    public String getDesc() {
        return desc;
    }

    public static int findVal(String desc) {
        if (POINT.desc.equals(desc)) {
            return POINT.val;
        }

        if (LINE_STRING.desc.equals(desc)) {
            return LINE_STRING.val;
        }

        if (POLYGON.desc.equals(desc)) {
            return POLYGON.val;
        }

        return UNKNOWN.val;
    }
}
