package com.dji.sdk.cloudapi.hms;

import com.dji.sdk.exception.CloudSDKException;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/7
 */
public enum HmsChargingRodIndexEnum {

    FRONT(0, "front", "前"),

    BACK(1, "back", "后"),

    LEFT(2, "left", "左"),

    RIGHT(3, "right", "右");

    private final int val;

    private final String en;

    private final String zh;

    HmsChargingRodIndexEnum(int val, String en, String zh) {
        this.val = val;
        this.en = en;
        this.zh = zh;
    }

    public int getVal() {
        return val;
    }

    public String getEn() {
        return en;
    }

    public String getZh() {
        return zh;
    }

    public static HmsChargingRodIndexEnum find(int val) {
        return Arrays.stream(HmsChargingRodIndexEnum.values()).filter(rod -> rod.val == val).findAny()
                .orElseThrow(() -> new CloudSDKException(HmsChargingRodIndexEnum.class, val));
    }
}
