package com.dji.sample.control.model.enums;

import lombok.Getter;

/**
 * @author sean
 * @version 1.3
 * @date 2023/1/13
 */
@Getter
public enum MqttAclAccessEnum {

    SUB(1),

    PUB(2),

    ALL(3);

    int value;

    MqttAclAccessEnum(int value) {
        this.value = value;
    }
}
