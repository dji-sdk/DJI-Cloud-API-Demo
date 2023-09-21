package com.dji.sdk.cloudapi.hms;

import com.dji.sdk.cloudapi.device.DeviceDomainEnum;
import com.dji.sdk.exception.CloudSDKException;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author sean
 * @version 1.1
 * @date 2022/7/7
 */
public enum HmsFaqIdEnum {

    DOCK_TIP("dock_tip_", DeviceDomainEnum.DOCK),

    FPV_TIP("fpv_tip_", DeviceDomainEnum.DRONE);

    private final String text;

    private final DeviceDomainEnum domain;

    @JsonValue
    public String getText() {
        return text;
    }

    public DeviceDomainEnum getDomain() {
        return domain;
    }

    HmsFaqIdEnum(String text, DeviceDomainEnum domain) {
        this.text = text;
        this.domain = domain;
    }

    public static HmsFaqIdEnum find(DeviceDomainEnum domain) {
        return Arrays.stream(values()).filter(faqIdEnum -> faqIdEnum.domain == domain).findAny()
                .orElseThrow(() -> new CloudSDKException(HmsFaqIdEnum.class, domain));
    }
}

