package com.dji.sample.component.mqtt.model;

/**
 * @author sean.zhou
 * @date 2021/11/22
 * @version 0.1
 */
public enum ServicesMethodEnum {

    LIVE_START_PUSH("live_start_push"),

    LIVE_STOP_PUSH("live_stop_push"),

    LIVE_SET_QUALITY("live_set_quality"),

    FLIGHTTASK_CREATE("flighttask_create"),

    UNKNOWN("unknown");

    private String method;

    ServicesMethodEnum(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}