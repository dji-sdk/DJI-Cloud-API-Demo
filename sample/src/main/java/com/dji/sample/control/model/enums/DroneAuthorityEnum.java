package com.dji.sample.control.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/1
 */
public enum DroneAuthorityEnum {

    FLIGHT(1), PAYLOAD(2);

    int val;

    DroneAuthorityEnum(int val) {
        this.val = val;
    }

    @JsonValue
    public int getVal() {
        return val;
    }

}
