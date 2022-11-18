package com.dji.sample.manage.model.enums;

import lombok.Getter;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
@Getter
public enum LogsFileMethodEnum {

    FILE_UPLOAD_LIST("fileupload_list"),

    FILE_UPLOAD_START("fileupload_start"),

    FILE_UPLOAD_UPDATE("fileupload_update"),

    UNKNOWN("unknown");

    private String method;

    LogsFileMethodEnum(String method) {
        this.method = method;
    }
}
