package com.dji.sample.common.error;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/25
 */
public enum StorageErrorEnum implements IErrorInfo {

    GENERATE_CREDENTIALS_ERROR(217001, "Failed to generate temporary credentials."),

    NO_BUCKET(217002, "The bucket does not exist."),

    ILLEGAL_PATH_FORMAT(217006, "Illegal path format."),

    FILE_CREATION_FAILED(217007, "File creation failed."),

    DIR_CREATION_FAILED(217008, "Directory creation failed");

    private String msg;

    private int code;

    StorageErrorEnum(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }

    @Override
    public Integer getErrorCode() {
        return code;
    }
}
