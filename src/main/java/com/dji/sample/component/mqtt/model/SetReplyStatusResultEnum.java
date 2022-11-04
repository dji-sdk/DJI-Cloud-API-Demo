package com.dji.sample.component.mqtt.model;

import lombok.Getter;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/28
 */
@Getter
public enum SetReplyStatusResultEnum {

    SUCCESS(0, "success"),

    FAILED(1, "failed"),

    TIMEOUT(2, "timeout"),

    UNKNOWN(-1, "unknown");

    int val;

    String desc;

    SetReplyStatusResultEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

}
