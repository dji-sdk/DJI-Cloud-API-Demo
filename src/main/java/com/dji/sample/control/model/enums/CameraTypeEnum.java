package com.dji.sample.control.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/3
 */
@Getter
public enum CameraTypeEnum {

    ZOOM("zoom"),

    WIDE("wide"),

    IR("ir");

    String type;

    CameraTypeEnum(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator
    public static CameraTypeEnum find(String cameraType) {
        return Arrays.stream(CameraTypeEnum.values()).filter(typeEnum -> typeEnum.type.equals(cameraType)).findAny().get();
    }
}
