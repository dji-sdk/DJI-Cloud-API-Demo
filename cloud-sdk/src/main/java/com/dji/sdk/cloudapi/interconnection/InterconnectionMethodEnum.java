package com.dji.sdk.cloudapi.interconnection;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/16
 */
public enum InterconnectionMethodEnum {

    CUSTOM_DATA_TRANSMISSION_TO_ESDK("custom_data_transmission_to_esdk"),

    CUSTOM_DATA_TRANSMISSION_TO_PSDK("custom_data_transmission_to_psdk"),

    ;

    private final String method;

    InterconnectionMethodEnum(String method) {
        this.method = method;
    }

    @JsonValue
    public String getMethod() {
        return method;
    }

}
