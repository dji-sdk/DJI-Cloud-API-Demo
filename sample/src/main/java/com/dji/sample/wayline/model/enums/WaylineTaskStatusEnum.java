package com.dji.sample.wayline.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sean
 * @version 1.3
 * @date 2023/2/1
 */
public enum WaylineTaskStatusEnum {

    PAUSE, RESUME;

    @JsonValue
    public int getVal() {
        return ordinal();
    }

}
