package com.dji.sample.component.mqtt.model;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @date 2021/11/22
 * @version 0.1
 */
public enum ServicesMethodEnum {

    LIVE_START_PUSH("live_start_push", false),

    LIVE_STOP_PUSH("live_stop_push", false),

    LIVE_SET_QUALITY("live_set_quality", false),

    FLIGHTTASK_CREATE("flighttask_create", false),

    DEBUG_MODE_OPEN("debug_mode_open", false),

    DEBUG_MODE_CLOSE("debug_mode_close", false),

    SUPPLEMENT_LIGHT_OPEN("supplement_light_open", false),

    SUPPLEMENT_LIGHT_CLOSE("supplement_light_close", false),

    RETURN_HOME("return_home", false),

    SDR_WORKMODE_SWITCH("sdr_workmode_switch", false),

    DEVICE_REBOOT("device_reboot", true),

    DRONE_OPEN("drone_open", true),

    DRONE_CLOSE("drone_close", true),

    DEVICE_CHECK("device_check", true),

    DRONE_FORMAT("drone_format", true),

    DEVICE_FORMAT("device_format", true),

    COVER_OPEN("cover_open", true),

    COVER_CLOSE("cover_close", true),

    PUTTER_OPEN("putter_open", true),

    PUTTER_CLOSE("putter_close", true),

    CHARGE_OPEN("charge_open", true),

    CHARGE_CLOSE("charge_close", true),

    OTA_CREATE("ota_create", true),

    FILE_UPLOAD_LIST("fileupload_list", false),

    FILE_UPLOAD_START("fileupload_start", true),

    FILE_UPLOAD_UPDATE("fileupload_update", false),

    UNKNOWN("unknown", false);

    private String method;

    private Boolean progress;

    ServicesMethodEnum(String method, Boolean progress) {
        this.method = method;
        this.progress = progress;
    }

    public static ServicesMethodEnum find(String method) {
        return Arrays.stream(ServicesMethodEnum.values())
                .filter(methodEnum -> methodEnum.method.equals(method))
                .findAny()
                .orElse(UNKNOWN);
    }

    public String getMethod() {
        return method;
    }

    public Boolean getProgress() {
        return progress;
    }
}