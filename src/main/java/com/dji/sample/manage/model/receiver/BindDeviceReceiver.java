package com.dji.sample.manage.model.receiver;

import lombok.Data;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/13
 */
@Data
public class BindDeviceReceiver {

    private String deviceBindingCode;

    private String organizationId;

    private String deviceCallsign;

    private String sn;

    private String deviceModelKey;
}
