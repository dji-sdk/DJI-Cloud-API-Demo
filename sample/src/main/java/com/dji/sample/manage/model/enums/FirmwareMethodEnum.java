package com.dji.sample.manage.model.enums;

import lombok.Getter;

/**
 * @author sean
 * @version 1.3
 * @date 2022/11/14
 */
@Getter
public enum FirmwareMethodEnum {

    OTA_CREATE("ota_create");

    private String method;

    FirmwareMethodEnum(String method) {
        this.method = method;
    }

}
