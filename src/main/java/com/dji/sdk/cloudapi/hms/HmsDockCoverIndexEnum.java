package com.dji.sdk.cloudapi.hms;

import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/7
 */
public enum HmsDockCoverIndexEnum {

    LEFT(0, "left", "左"),

    RIGHT(1, "right", "右");

    private final int val;

    private final String en;

    private final String zh;

    HmsDockCoverIndexEnum(int val, String en, String zh) {
        this.val = val;
        this.en = en;
        this.zh = zh;
    }

    @JsonValue
    public int getVal() {
        return val;
    }

    public String getEn() {
        return en;
    }

    public String getZh() {
        return zh;
    }

    public static HmsDockCoverIndexEnum find(int val) {
        return Arrays.stream(HmsDockCoverIndexEnum.values()).filter(dockCover -> dockCover.val == val).findAny()
                .orElseThrow(() -> new CloudSDKException(HmsDockCoverIndexEnum.class, val));
    }
}
