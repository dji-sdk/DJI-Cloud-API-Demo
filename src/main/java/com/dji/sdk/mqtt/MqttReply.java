package com.dji.sdk.mqtt;

import com.dji.sdk.common.IErrorInfo;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/13
 */
public class MqttReply<T> {

    public static final int CODE_ERROR = -1;

    public static final int CODE_SUCCESS = 0;

    private Integer result;

    private T output;

    private MqttReply() {
    }

    @Override
    public String toString() {
        return "MqttReply{" +
                "result=" + result +
                ", output=" + output +
                '}';
    }

    private MqttReply(T output) {
        this.output = output;
    }

    private MqttReply(Integer result, T output) {
        this.result = result;
        this.output = output;
    }

    public static MqttReply error(IErrorInfo errorInfo) {
        return new MqttReply<String>(errorInfo.getCode(), errorInfo.getMessage());
    }

    public static MqttReply error(String message) {
        return new MqttReply<String>(CODE_ERROR, message);
    }

    public static <T> MqttReply<T> success(T data) {
        return new MqttReply<T>(CODE_SUCCESS, data);
    }

    public static MqttReply success() {
        return new MqttReply().setResult(CODE_SUCCESS);
    }

    public Integer getResult() {
        return result;
    }

    public MqttReply<T> setResult(Integer result) {
        this.result = result;
        return this;
    }

    public MqttReply<T> setOutput(T output) {
        this.output = output;
        return this;
    }

    public T getOutput() {
        return output;
    }
}
