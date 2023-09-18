package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/5/26
 */
@Schema(description = "device subType", enumAsRef = true)
public enum DeviceSubTypeEnum {

    ZERO(0),

    ONE(1),

    TWO(2),

    _65535(65535);

    private final int subType;

    DeviceSubTypeEnum(int subType) {
        this.subType = subType;
    }

    @JsonValue
    public int getSubType() {
        return subType;
    }

    @JsonCreator
    public static DeviceSubTypeEnum find(int subType) {
        return Arrays.stream(values()).filter(typeEnum -> typeEnum.subType == subType).findAny()
                .orElseThrow(() -> new CloudSDKException(DeviceSubTypeEnum.class, subType));
    }
}
