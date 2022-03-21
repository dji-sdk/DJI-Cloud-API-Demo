package com.dji.sample.manage.model.enums;

/**
 * @author sean.zhou
 * @date 2021/11/22
 * @version 0.1
 */
public enum LiveMethodEnum {

    LIVE_START_PUSH("live_start_push"),

    LIVE_STOP_PUSH("live_stop_push"),

    LIVE_SET_QUALITY("live_set_quality"),

    UNKNOWN("unknown");

    private String method;

    LiveMethodEnum(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}