package com.dji.sample.component.mqtt.model;

/**
 *
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
public enum StateDataEnum {

    FIRMWARE_VERSION("firmware_version"),

    LIVE_CAPACITY("live_capacity"),

    PAYLOADS("payloads");

    private String desc;

    StateDataEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }
}
