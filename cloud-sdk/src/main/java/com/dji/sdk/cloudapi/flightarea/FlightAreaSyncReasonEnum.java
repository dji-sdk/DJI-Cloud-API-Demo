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
public enum FlightAreaSyncReasonEnum {

    SUCCESS(0, "success"),

    PARSE_FILE_FAILED(1, "Failed to parse file information returned from the cloud."),

    RETRIEVE_FILE_FAILED(2, "Failed to retrieve file information from the aircraft's end."),

    DOWNLOAD_FILE_FAILED(3, "Failed to download the file from the cloud."),

    LINK_FLIPPING_FAILED(4, "Link flipping failed."),

    FILE_TRANSMISSION_FAILED(5, "File transmission failed."),

    DISABLE_FAILED(6, "Filed to disable."),

    FILE_DELETION_FAILED(7, "File deletion failed."),

    FILE_LOADING_FAILED(8, "Failed to load file on drone."),

    ENABLE_FAILED(9, "Filed to enable."),

    TURN_OFF_ENHANCED_FAILED(10, "Failed to turn off enhanced image transmission."),

    POWER_ON_FAILED(11, "Failed to power on the drone."),

    CHECK_FAILED(12, "The checksum check failed."),

    SYNCHRONIZATION_TIMED_OUT(13, "Synchronization exception timed out."),

    ;

    private final int reason;

    private final String msg;

    FlightAreaSyncReasonEnum(int reason, String msg) {
        this.reason = reason;
        this.msg = msg;
    }

    @JsonValue
    public int getReason() {
        return reason;
    }

    public String getMsg() {
        return msg;
    }

    @JsonCreator
    public static FlightAreaSyncReasonEnum find(int reason) {
        return Arrays.stream(values()).filter(reasonEnum -> reasonEnum.reason == reason).findAny()
            .orElseThrow(() -> new CloudSDKException(FlightAreaSyncReasonEnum.class, reason));
    }

}
