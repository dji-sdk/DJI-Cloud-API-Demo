package com.dji.sdk.cloudapi.log;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
public enum LogMethodEnum {

    FILE_UPLOAD_LIST("fileupload_list"),

    FILE_UPLOAD_START("fileupload_start"),

    FILE_UPLOAD_UPDATE("fileupload_update");

    private final String method;

    LogMethodEnum(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
