package com.dji.sample.wayline.model.enums;

import lombok.Getter;

/**
 * @author sean
 * @version 1.3
 * @date 2022/9/26
 */
@Getter
public enum WaylineTaskTypeEnum {

    IMMEDIATE(0),

    TIMED(1);

    int val;

    WaylineTaskTypeEnum(int val) {
        this.val = val;
    }
}
