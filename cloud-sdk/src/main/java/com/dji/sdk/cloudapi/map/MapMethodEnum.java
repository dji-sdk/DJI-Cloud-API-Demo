package com.dji.sdk.cloudapi.map;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public enum MapMethodEnum {

    OFFLINE_MAP_UPDATE("offline_map_update"),

    ;

    private final String method;

    MapMethodEnum(String method) {
        this.method = method;
    }

    @JsonValue
    public String getMethod() {
        return method;
    }
}
