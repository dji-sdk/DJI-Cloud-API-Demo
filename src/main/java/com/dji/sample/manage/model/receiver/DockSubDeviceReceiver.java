package com.dji.sample.manage.model.receiver;

import lombok.Data;

/**
 * @author sean
 * @version 1.0
 * @date 2022/5/11
 */
@Data
public class DockSubDeviceReceiver {

    private String deviceSn;

    private Integer deviceOnlineStatus;

    private Integer devicePaired;

    private String deviceModelKey;
}
