package com.dji.sdk.cloudapi.map;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/20
 */
public enum OfflineMapSyncReasonEnum {

    SUCCESS(0, "success"),

    PARSE_FILE_FAILED(1, "Failed to parse the file information returned by the cloud."),

    OBTAIN_DRONE_FILE_FAILED(2, "Failed to obtain aircraft file information."),

    DOWNLOAD_FILE_FAILED(3, "Failed to download file from cloud."),

    LINK_ROLLOVER_FAILED(4, "Failed to rollover the link."),

    FILE_TRANSFER_FAILED(5, "Failed to transfer file."),

    DISABLE_OFFLINE_MAP_FAILED(6, "Failed to disable offline map."),

    DELETE_FILE_FAILED(7, "Failed to delete file."),

    LOAD_FILE_FAILED(8, "Failed to load the file on the device side."),

    ENABLE_OFFLINE_MAP_FAILED(9, "Failed to enable offline map."),

    ;

    private final int reason;

    private final String msg;

    OfflineMapSyncReasonEnum(int reason, String msg) {
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
    public static OfflineMapSyncReasonEnum find(int reason) {
        return Arrays.stream(values()).filter(reasonEnum -> reasonEnum.reason == reason).findAny()
            .orElseThrow(() -> new CloudSDKException(OfflineMapSyncReasonEnum.class, reason));
    }

}
