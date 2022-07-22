package com.dji.sample.component.mqtt.model;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/25
 */
public enum RequestsMethodEnum {

    STORAGE_CONFIG_GET("storage_config_get"),

    AIRPORT_BIND_STATUS("airport_bind_status"),

    AIRPORT_ORGANIZATION_BIND("airport_organization_bind"),

    AIRPORT_ORGANIZATION_GET("airport_organization_get"),

    UNKNOWN("Unknown");

    private String method;

    RequestsMethodEnum(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public static RequestsMethodEnum find(String method) {
        return Arrays.stream(RequestsMethodEnum.values())
                .filter(methodEnum -> methodEnum.method.equals(method))
                .findAny()
                .orElse(UNKNOWN);
    }
}
