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
public enum WindDirectionEnum {

    NO(0),

    NORTH(1),

    NORTHEAST(2),

    EAST(3),

    SOUTHEAST(4),

    SOUTH(5),

    SOUTHWEST(6),

    WEST(7),

    NORTHWEST(8),
    ;

    private final int direction;

    WindDirectionEnum(int direction) {
        this.direction = direction;
    }

    @JsonValue
    public int getDirection() {
        return direction;
    }

    @JsonCreator
    public static WindDirectionEnum find(int direction) {
        return Arrays.stream(values()).filter(directionEnum -> directionEnum.direction == direction).findAny()
            .orElseThrow(() -> new CloudSDKException(WindDirectionEnum.class, direction));
    }

}
