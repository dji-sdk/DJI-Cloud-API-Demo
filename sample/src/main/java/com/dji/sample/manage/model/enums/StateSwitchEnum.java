package com.dji.sample.manage.model.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/28
 */
public enum StateSwitchEnum {

    DISABLE, ENABLE;

    public static Optional<StateSwitchEnum> find(int value) {
        return Arrays.stream(StateSwitchEnum.values()).filter(state -> state.ordinal() == value).findAny();
    }
}
