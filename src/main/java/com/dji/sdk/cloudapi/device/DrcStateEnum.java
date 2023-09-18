package com.dji.sdk.cloudapi.device;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/2/28
 */
public enum DrcStateEnum {

    DISCONNECTED(0),

    CONNECTING(1),

    CONNECTED(2);

    private final int state;

    DrcStateEnum(int state) {
        this.state = state;
    }

    @JsonValue
    public int getState() {
        return state;
    }

    @JsonCreator
    public static DrcStateEnum find(int state) {
        return Arrays.stream(values()).filter(drcState -> drcState.state == state).findAny()
                .orElseThrow(() -> new CloudSDKException(DrcStateEnum.class, state));
    }
}
