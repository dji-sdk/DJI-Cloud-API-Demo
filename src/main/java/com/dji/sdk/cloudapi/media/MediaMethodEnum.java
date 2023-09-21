package com.dji.sdk.cloudapi.media;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
public enum MediaMethodEnum {

    UPLOAD_FLIGHTTASK_MEDIA_PRIORITIZE("upload_flighttask_media_prioritize");

    private final String method;

    MediaMethodEnum(String method) {
        this.method = method;
    }

    @JsonValue
    public String getMethod() {
        return method;
    }
}
