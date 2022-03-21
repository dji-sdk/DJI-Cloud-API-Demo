package com.dji.sample.manage.model.enums;

/**
 *
 * @author sean.zhou
 * @date 2021/11/18
 * @version 0.1
 */
public enum StateDataEnum {

    BATTERIES("batteries"),

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
