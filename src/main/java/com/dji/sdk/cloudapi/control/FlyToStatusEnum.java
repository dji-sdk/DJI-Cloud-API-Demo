package com.dji.sdk.cloudapi.control;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/14
 */
public enum FlyToStatusEnum {

    WAYLINE_PROGRESS("wayline_progress", "The FlyTo job is in progress."),

    WAYLINE_FAILED("wayline_failed", "The FlyTo job execution failed."),

    WAYLINE_OK("wayline_ok", "The FlyTo job executed successfully."),

    WAYLINE_CANCEL("wayline_cancel", "The FlyTo job is closed.");

    private final String status;

    private final String message;

    FlyToStatusEnum(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static FlyToStatusEnum find(String status) {
        return Arrays.stream(values()).filter(statusEnum -> statusEnum.status.equals(status)).findAny()
                .orElseThrow(() -> new CloudSDKException(FlyToStatusEnum.class, status));
    }
}
