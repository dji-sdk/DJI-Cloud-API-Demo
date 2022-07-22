package com.dji.sample.component.mqtt.model;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/1
 */
public enum EventsMethodEnum {

    FLIGHT_TASK_PROGRESS("flighttask_progress"),

    FILE_UPLOAD_CALLBACK("file_upload_callback"),

    HMS("hms"),

    UNKNOWN("Unknown");

    private String method;

    EventsMethodEnum(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public static EventsMethodEnum find(String method) {
        return Arrays.stream(EventsMethodEnum.values())
                .filter(methodEnum -> methodEnum.method.equals(method))
                .findAny()
                .orElse(UNKNOWN);
    }
}
