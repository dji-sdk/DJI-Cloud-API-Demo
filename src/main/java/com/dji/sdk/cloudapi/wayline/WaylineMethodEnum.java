package com.dji.sdk.cloudapi.wayline;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
public enum WaylineMethodEnum {

    FLIGHTTASK_CREATE("flighttask_create"),

    FLIGHTTASK_PREPARE("flighttask_prepare"),

    FLIGHTTASK_EXECUTE("flighttask_execute"),

    FLIGHTTASK_UNDO("flighttask_undo"),

    FLIGHTTASK_PAUSE("flighttask_pause"),

    FLIGHTTASK_RECOVERY("flighttask_recovery"),

    RETURN_HOME("return_home"),

    RETURN_HOME_CANCEL("return_home_cancel");

    private final String method;

    WaylineMethodEnum(String method) {
        this.method = method;
    }

    @JsonValue
    public String getMethod() {
        return this.method;
    }

}
