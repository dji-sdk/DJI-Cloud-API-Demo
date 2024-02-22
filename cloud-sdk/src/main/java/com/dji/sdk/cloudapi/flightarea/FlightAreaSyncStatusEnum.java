package com.dji.sdk.cloudapi.flightarea;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/17
 */
public enum FlightAreaSyncStatusEnum {

    WAIT_SYNC("wait_sync", false),

    SWITCH_FAIL("switch_fail", false),

    SYNCHRONIZING("synchronizing", false),

    SYNCHRONIZED("synchronized", true),

    FAIL("fail", true),

    ;

    private final String status;

    private final boolean end;

    FlightAreaSyncStatusEnum(String status, boolean end) {
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
    public static FlightAreaSyncStatusEnum find(String status) {
        return Arrays.stream(values()).filter(statusEnum -> statusEnum.status.equals(status)).findAny()
            .orElseThrow(() -> new CloudSDKException(FlightAreaSyncStatusEnum.class, status));
    }
}
