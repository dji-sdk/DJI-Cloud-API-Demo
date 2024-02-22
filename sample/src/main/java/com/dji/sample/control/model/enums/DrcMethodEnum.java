package com.dji.sample.control.model.enums;

import lombok.Getter;

/**
 * @author sean
 * @version 1.3
 * @date 2023/1/11
 */
@Getter
public enum DrcMethodEnum {

    DRC_MODE_ENTER("drc_mode_enter"),

    DRC_MODE_EXIT("drc_mode_exit");

    String method;

    DrcMethodEnum(String method) {
        this.method = method;
    }
}
