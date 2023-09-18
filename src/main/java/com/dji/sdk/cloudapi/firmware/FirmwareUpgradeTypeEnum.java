package com.dji.sdk.cloudapi.firmware;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/15
 */
public enum FirmwareUpgradeTypeEnum {

    /**
     *  to upgraded
     */
    NORMAL_UPGRADE(2),

    /**
     * A consistency upgrade is required.
     */
    CONSISTENT_UPGRADE(3);

    private final int type;

    FirmwareUpgradeTypeEnum(int type) {
        this.type = type;
    }

    @JsonValue
    public int getType() {
        return type;
    }

    @JsonCreator
    public static FirmwareUpgradeTypeEnum find(int type) {
        return Arrays.stream(values()).filter(typeEnum -> typeEnum.type == type).findAny()
                .orElseThrow(() -> new CloudSDKException(FirmwareUpgradeTypeEnum.class, type));

    }

}
