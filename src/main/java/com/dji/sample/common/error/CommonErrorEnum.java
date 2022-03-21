package com.dji.sample.common.error;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum CommonErrorEnum implements IErrorInfo {

    SYSTEM_ERROR(600500, "system error"),

    SECRET_INVALID(600100, "secret invalid"),

    NO_TOKEN(600101, "accss_token is null"),

    TOKEN_EXPIRED(600102, "token is expired"),

    TOKEN_INVALID(600103, "token invalid"),

    SIGN_INVALID(600104, "sign invalid");

    private String msg;

    private int code;

    CommonErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getErrorMsg() {
        return this.msg;
    }

    @Override
    public Integer getErrorCode() {
        return this.code;
    }
}
