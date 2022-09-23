package com.dji.sample.component.mqtt.model;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.2
 * @date 2022/8/17
 */
@Getter
public enum EventsResultStatusEnum {

    SENT("sent", false),

    IN_PROGRESS("in_progress", false),

    OK("ok", true),

    PAUSED("paused", false),

    REJECTED("rejected", true),

    FAILED("failed", true),

    CANCELED("canceled", true),

    TIMEOUT("timeout", true),

    UNKNOWN("unknown", false);

    String desc;

    Boolean end;

    EventsResultStatusEnum(String desc, Boolean end) {
        this.desc = desc;
        this.end = end;
    }

    public static EventsResultStatusEnum find(String desc) {
        return Arrays.stream(EventsResultStatusEnum.values())
                .filter(status -> status.desc.equals(desc))
                .findFirst().orElse(UNKNOWN);
    }
}
