package com.dji.sdk.cloudapi.firmware;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/17
 */
public enum OtaProgressStatusEnum {

    SENT("sent", false),

    IN_PROGRESS("in_progress", false),

    OK("ok", true),

    PAUSED("paused", false),

    REJECTED("rejected", true),

    FAILED("failed", true),

    CANCELED("canceled", true),

    TIMEOUT("timeout", true);

    private final String status;

    private final boolean end;

    OtaProgressStatusEnum(String status, boolean end) {
        this.status = status;
        this.end = end;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    public boolean isEnd() {
        return end;
    }

    @JsonCreator
    public static OtaProgressStatusEnum find(String status) {
        return Arrays.stream(values()).filter(statusEnum -> statusEnum.status.equals(status)).findAny()
                .orElseThrow(() -> new CloudSDKException(OtaProgressStatusEnum.class, status));
    }
}


