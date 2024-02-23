package com.dji.sdk.cloudapi.map;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/15
 */
@Schema(description = "<p>0: custom element group <p/><p>1: default element group <p/><p>2: APP shared element group " +
        "(type=2 is an APP element group, PILOT will add map elements to this element group by default, " +
        "and there must be an APP shared element group. " +
        "It is recommended that in the same workspace, there are And there is only one APP shared element group)<p/>",
        enumAsRef = true, type = "int", allowableValues = {"0", "1", "2"})
public enum GroupTypeEnum {

    CUSTOM(0),

    DEFAULT(1),

    SHARED(2);

    private final int type;

    GroupTypeEnum(int type) {
        this.type = type;
    }

    @JsonValue
    public int getType() {
        return type;
    }

    @JsonCreator
    public static GroupTypeEnum find(int type) {
        return Arrays.stream(values()).filter(typeEnum -> typeEnum.type == type).findAny()
            .orElseThrow(() -> new CloudSDKException(GroupTypeEnum.class, type));
    }
}
