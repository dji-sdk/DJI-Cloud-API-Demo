package com.dji.sample.wayline.model.enums;

import lombok.Getter;

/**
 * @author sean
 * @version 1.3
 * @date 2022/9/26
 */
@Getter
public enum WaylineTemplateTypeEnum {

    WAYPOINT(0),

    MAPPING_2D(1),

    MAPPING_3D(2),

    MAPPING_STRIP(4);

    int val;

    WaylineTemplateTypeEnum(int val) {
        this.val = val;
    }
}
