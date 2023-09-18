package com.dji.sdk.mqtt.drc;

import com.dji.sdk.cloudapi.wayline.WaylineErrorCodeEnum;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/22
 */
public class DrcUpData<T> {

    private WaylineErrorCodeEnum result;

    private T output;

    public DrcUpData() {
    }

    @Override
    public String toString() {
        return "DrcUpData{" +
                "result=" + result +
                ", output=" + output +
                '}';
    }

    public WaylineErrorCodeEnum getResult() {
        return result;
    }

    public DrcUpData<T> setResult(WaylineErrorCodeEnum result) {
        this.result = result;
        return this;
    }

    public T getOutput() {
        return output;
    }

    public DrcUpData<T> setOutput(T output) {
        this.output = output;
        return this;
    }
}