package com.dji.sdk.mqtt.events;

import com.dji.sdk.cloudapi.control.ControlErrorCodeEnum;
import com.dji.sdk.cloudapi.debug.DebugErrorCodeEnum;
import com.dji.sdk.cloudapi.firmware.FirmwareErrorCodeEnum;
import com.dji.sdk.cloudapi.log.LogErrorCodeEnum;
import com.dji.sdk.cloudapi.wayline.WaylineErrorCodeEnum;
import com.dji.sdk.common.CommonErrorEnum;
import com.dji.sdk.common.ErrorCodeSourceEnum;
import com.dji.sdk.common.IErrorInfo;
import com.dji.sdk.mqtt.MqttReply;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sean
 * @version 1.7
 * @date 2023/7/14
 */
public class EventsErrorCode implements IErrorInfo {

    private static final int MOD = 100_000;

    private ErrorCodeSourceEnum source;

    private IEventsErrorCode errorCode;

    private boolean success;

    private Integer sourceCode;

    @Override
    public String toString() {
        return "{" +
                "errorCode=" + getCode() +
                ", errorMsg=" + getMessage() +
                '}';
    }

    @JsonCreator
    public EventsErrorCode(int code) {
        this.sourceCode = code;
        if (MqttReply.CODE_SUCCESS == code) {
            this.success = true;
            this.errorCode = CommonErrorEnum.SUCCESS;
            return;
        }
        this.source = ErrorCodeSourceEnum.find(code / MOD);
        this.errorCode = DebugErrorCodeEnum.find(code);
        if (errorCode.getCode() != -1) {
            return;
        }
        this.errorCode = ControlErrorCodeEnum.find(code);
        if (errorCode.getCode() != -1) {
            return;
        }
        this.errorCode = LogErrorCodeEnum.find(code);
        if (errorCode.getCode() != -1) {
            return;
        }
        this.errorCode = FirmwareErrorCodeEnum.find(code);
        if (errorCode.getCode() != -1) {
            return;
        }
        this.errorCode = WaylineErrorCodeEnum.find(code);
        if (errorCode.getCode() != -1) {
            return;
        }
        this.errorCode = CommonErrorEnum.find(code);
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    @JsonValue
    public Integer getCode() {
        return sourceCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public ErrorCodeSourceEnum getSource() {
        return source;
    }
}
