package com.dji.sample.control.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
@Getter
public enum BatteryStoreModeEnum {

    PLAN(1),

    EMERGENCY(2);

    Integer mode;

    BatteryStoreModeEnum(Integer mode) {
        this.mode = mode;
    }

    public static Optional<BatteryStoreModeEnum> find(int mode) {
        return Arrays.stream(BatteryStoreModeEnum.values()).filter(modeEnum -> modeEnum.mode == mode).findAny();
    }
}
