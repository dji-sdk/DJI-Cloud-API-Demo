package com.dji.sdk.mqtt.services;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/22
 */
public class ServicesReplyData<T> {

    private ServicesErrorCode result;

    private T output;

    public ServicesReplyData() {
    }

    @Override
    public String toString() {
        return "DrcUpData{" +
                "result=" + result +
                ", output=" + output +
                '}';
    }

    public ServicesErrorCode getResult() {
        return result;
    }

    public ServicesReplyData<T> setResult(ServicesErrorCode result) {
        this.result = result;
        return this;
    }

    public T getOutput() {
        return output;
    }

    public ServicesReplyData<T> setOutput(T output) {
        this.output = output;
        return this;
    }
}