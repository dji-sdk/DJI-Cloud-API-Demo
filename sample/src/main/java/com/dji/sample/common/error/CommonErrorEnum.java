package com.dji.sample.common.error;

import com.dji.sdk.common.IErrorInfo;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum CommonErrorEnum implements IErrorInfo {

    ILLEGAL_ARGUMENT(200001, "illegal argument"),

    REDIS_DATA_NOT_FOUND(201404, "Redis data does not exist."),

    DEVICE_OFFLINE(212015, "Device is offline."),

    GET_ORGANIZATION_FAILED(210230, "Failed to get organization."),

    DEVICE_BINDING_FAILED(210231, "Failed to bind device."),

    NON_REPEATABLE_BINDING(210232, "The device has been bound to another organization and can't be bound repeatedly."),

    GET_DEVICE_BINDING_STATUS_FAILED(210233, "Failed to get device binding status."),

    SYSTEM_ERROR(600500, "system error"),

    SECRET_INVALID(600100, "secret invalid"),

    NO_TOKEN(600101, "token is null"),

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
    public String getMessage() {
        return this.msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

}
