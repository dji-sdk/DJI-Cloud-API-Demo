package com.dji.sample.control.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/25
 */
@Getter
public enum LinkWorkModeEnum {

    SDR_ONLY(0),

    SDR_WITH_4G(1);

    int mode;

    LinkWorkModeEnum(Integer mode) {
        this.mode = mode;
    }

    public static Optional<LinkWorkModeEnum> find(int mode) {
        return Arrays.stream(LinkWorkModeEnum.values()).filter(modeEnum -> modeEnum.mode == mode).findAny();
    }
}
