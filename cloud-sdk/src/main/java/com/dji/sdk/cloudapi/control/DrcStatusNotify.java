package com.dji.sdk.cloudapi.control;

import com.dji.sdk.cloudapi.device.DrcStateEnum;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/17
 */
public class DrcStatusNotify {

    private DrcStatusErrorEnum result;

    private DrcStateEnum drcState;

    public DrcStatusNotify() {
    }

    @Override
    public String toString() {
        return "DrcStatusNotify{" +
                "result=" + result +
                ", drcState=" + drcState +
                '}';
    }

    public DrcStatusErrorEnum getResult() {
        return result;
    }

    public DrcStatusNotify setResult(DrcStatusErrorEnum result) {
        this.result = result;
        return this;
    }

    public DrcStateEnum getDrcState() {
        return drcState;
    }

    public DrcStatusNotify setDrcState(DrcStateEnum drcState) {
        this.drcState = drcState;
        return this;
    }
}
