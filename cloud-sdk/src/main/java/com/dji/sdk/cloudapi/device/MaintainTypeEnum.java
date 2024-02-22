package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/30
 */
public enum MaintainTypeEnum {

    NO(0),

    DRONE_BASIC(1),

    DRONE_ROUTINE(2),

    DRONE_DEEP(3),

    DOCK_ROUTINE(17),

    DOCK_DEEP(18),
    ;

    private final int type;

    MaintainTypeEnum(int type) {
        this.type = type;
    }

    @JsonValue
    public int getType() {
        return type;
    }

    @JsonCreator
    public static MaintainTypeEnum find(int type) {
        return Arrays.stream(values()).filter(typeEnum -> typeEnum.type == type).findAny()
            .orElseThrow(() -> new CloudSDKException(MaintainTypeEnum.class, type));
    }

}
