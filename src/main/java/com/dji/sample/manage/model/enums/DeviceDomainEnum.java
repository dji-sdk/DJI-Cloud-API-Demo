package com.dji.sample.manage.model.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 *
 * @author sean.zhou
 * @date 2021/11/15
 * @version 0.1
 */
@Getter
public enum DeviceDomainEnum {

    SUB_DEVICE(0),

    GATEWAY(2),

    PAYLOAD(1),

    DOCK (3),

    UNKNOWN(-1);

    int val;

    DeviceDomainEnum(int val) {
        this.val = val;
    }

    public static DeviceDomainEnum find(int val) {
        return Arrays.stream(values()).filter(domainEnum -> domainEnum.val == val).findAny().orElse(UNKNOWN);
    }
}
