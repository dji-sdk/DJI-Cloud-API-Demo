package com.dji.sample.manage.model.enums;

import lombok.Getter;

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

    DOCK (3);

    int val;

    DeviceDomainEnum(int val) {
        this.val = val;
    }
}
