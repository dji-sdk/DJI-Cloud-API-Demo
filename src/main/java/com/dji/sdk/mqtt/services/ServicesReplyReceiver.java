package com.dji.sdk.mqtt.services;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/22
 */
public class ServicesReplyReceiver<T> {

    private ServicesErrorCode result;

    private T info;

    private T output;

    private T files;

    public ServicesReplyReceiver() {
    }

    @Override
    public String toString() {
        return "ServicesReplyReceiver{" +
                "result=" + result +
                ", info=" + info +
                ", output=" + output +
                ", files=" + files +
                '}';
    }

    public ServicesErrorCode getResult() {
        return result;
    }

    public ServicesReplyReceiver<T> setResult(ServicesErrorCode result) {
        this.result = result;
        return this;
    }

    public T getInfo() {
        return info;
    }

    public ServicesReplyReceiver<T> setInfo(T info) {
        this.info = info;
        return this;
    }

    public T getOutput() {
        return output;
    }

    public ServicesReplyReceiver<T> setOutput(T output) {
        this.output = output;
        return this;
    }

    public T getFiles() {
        return files;
    }

    public ServicesReplyReceiver<T> setFiles(T files) {
        this.files = files;
        return this;
    }
}