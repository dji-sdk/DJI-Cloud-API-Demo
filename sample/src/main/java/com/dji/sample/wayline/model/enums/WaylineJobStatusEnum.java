package com.dji.sample.wayline.model.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.3
 * @date 2022/9/26
 */
@Getter
public enum WaylineJobStatusEnum {

    PENDING(1, false),

    IN_PROGRESS(2, false),

    SUCCESS(3, true),

    CANCEL(4, true),

    FAILED(5, true),

    PAUSED(6, false),

    UNKNOWN(-1, true);

    int val;

    Boolean end;

    WaylineJobStatusEnum(int val, boolean end) {
        this.end = end;
        this.val = val;
    }

    public static WaylineJobStatusEnum find(int val) {
        return Arrays.stream(WaylineJobStatusEnum.values()).filter(statue -> statue.val == val).findAny().orElse(UNKNOWN);
    }
}
