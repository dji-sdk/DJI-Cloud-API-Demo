package com.dji.sample.manage.model.enums;

import com.dji.sample.component.mqtt.model.EventsResultStatusEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author sean
 * @version 1.2
 * @date 2022/9/8
 */
@Getter
public enum DeviceLogsStatusEnum {

    UPLOADING(1, EventsResultStatusEnum.IN_PROGRESS),

    DONE(2, EventsResultStatusEnum.OK),

    CANCELED(3, EventsResultStatusEnum.CANCELED),

    FAILED(4, EventsResultStatusEnum.FAILED, EventsResultStatusEnum.REJECTED, EventsResultStatusEnum.TIMEOUT),

    UNKNOWN(-1);

    int val;

    HashSet<EventsResultStatusEnum> status;

    DeviceLogsStatusEnum(int val, EventsResultStatusEnum... status) {
        this.status = new HashSet<>();
        Collections.addAll(this.status, status);
        this.val = val;
    }

    public static DeviceLogsStatusEnum find(EventsResultStatusEnum status) {
        return Arrays.stream(DeviceLogsStatusEnum.values()).filter(element -> element.status.contains(status)).findAny().orElse(UNKNOWN);
    }
}
