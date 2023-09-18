package com.dji.sdk.cloudapi.firmware;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
public enum FirmwareMethodEnum {

    OTA_CREATE("ota_create");

    private final String method;

    FirmwareMethodEnum(String method) {
        this.method = method;
    }

    @JsonValue
    public String getMethod() {
        return method;
    }
}
