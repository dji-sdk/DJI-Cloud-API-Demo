package com.dji.sdk.cloudapi.hms;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/7
 */
public enum HmsFormatKeyEnum {

    ALARM_ID("%alarmid"),

    COMPONENT_INDEX("%component_index"),

    INDEX("%index"),

    BATTERY_INDEX("%battery_index"),

    DOCK_COVER_INDEX("%dock_cover_index"),

    CHARGING_ROD_INDEX("%charging_rod_index");

//    public static final char KEY_START = '%';

    private final String key;

    HmsFormatKeyEnum(String key) {
        this.key = key;
    }

    @JsonValue
    public String getKey() {
        return key;
    }

    public static HmsFormatKeyEnum find(String key) {
        return Arrays.stream(HmsFormatKeyEnum.values()).filter(format -> format.getKey().equals(key)).findAny()
                .orElseThrow(() -> new CloudSDKException(HmsFormatKeyEnum.class, key));
    }
}
