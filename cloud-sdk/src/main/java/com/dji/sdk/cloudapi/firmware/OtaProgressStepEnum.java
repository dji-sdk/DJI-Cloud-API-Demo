package com.dji.sdk.cloudapi.firmware;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/6/28
 */
public enum OtaProgressStepEnum {

    DOWNLOADING(1),

    UPGRADING(2);

    private final int step;

    OtaProgressStepEnum(int step) {
        this.step = step;
    }

    @JsonValue
    public int getStep() {
        return step;
    }

    @JsonCreator
    public static OtaProgressStepEnum find(int step) {
        return Arrays.stream(values()).filter(stepEnum -> stepEnum.step == step).findAny()
                .orElseThrow(() -> new CloudSDKException(OtaProgressStepEnum.class, step));
    }
}
