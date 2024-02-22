package com.dji.sample.media.model;

import lombok.Getter;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/23
 */
@Getter
public enum MediaMethodEnum {

    UPLOAD_FLIGHT_TASK_MEDIA_PRIORITIZE("upload_flighttask_media_prioritize");

    private String method;

    MediaMethodEnum(String method) {
        this.method = method;
    }
}
