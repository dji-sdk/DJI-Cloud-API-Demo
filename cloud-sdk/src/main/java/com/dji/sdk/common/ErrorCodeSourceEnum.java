
package com.dji.sdk.common;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public enum ErrorCodeSourceEnum {

    //fix: Cannot construct instance of `com.dji.sdk.cloudapi.device.DockLiveErrorStatus`, problem: com.dji.sdk.common.ErrorCodeSourceEnum has unknown data: [0]
    UNKNOWN(0),

    DEVICE(3),

    DOCK(5),

    PILOT(6);

    private final int source;

    ErrorCodeSourceEnum(int source) {
        this.source = source;
    }

    @JsonValue
    public int getSource() {
        return source;
    }

    @JsonCreator
    public static ErrorCodeSourceEnum find(int source) {
        return Arrays.stream(values()).filter(error -> error.source == source).findAny()
                .orElseThrow(() -> new CloudSDKException(ErrorCodeSourceEnum.class, source));
    }
}
