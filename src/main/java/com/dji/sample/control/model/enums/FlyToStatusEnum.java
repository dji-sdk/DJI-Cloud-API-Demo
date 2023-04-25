package com.dji.sample.control.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.4
 * @date 2023/3/14
 */
public enum FlyToStatusEnum {

    WAYLINE_PROGRESS("wayline_progress", "The FlyTo job is in progress."),

    WAYLINE_FAILED("wayline_failed", "The Fly To task execution failed."),

    WAYLINE_OK("wayline_ok", "The FlyTo job executed successfully."),

    WAYLINE_CANCEL("wayline_cancel", "The FlyTo job is closed.");

    String status;

    String message;

    FlyToStatusEnum(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static FlyToStatusEnum find(String status) {
        return Arrays.stream(values()).filter(statusEnum -> statusEnum.status.equals(status)).findAny().get();
    }
}
