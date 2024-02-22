package com.dji.sample.manage.model.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/9
 */
@Getter
public enum LogsFileUpdateMethodEnum {

    CANCEL("cancel"),

    UNKNOWN("unknown");

    String method;

    LogsFileUpdateMethodEnum(String method) {
        this.method = method;
    }

    public static LogsFileUpdateMethodEnum find(String method) {
        return Arrays.stream(LogsFileUpdateMethodEnum.values()).filter(e -> e.method.equals(method)).findAny().orElse(UNKNOWN);
    }
}
