package com.dji.sdk.cloudapi.hms;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/7
 */
public enum HmsInTheSkyEnum {

    IN_THE_SKY("_in_the_sky");

    private final String text;

    HmsInTheSkyEnum(String text) {
        this.text = text;
    }

    @JsonValue
    public String getText() {
        return text;
    }
}