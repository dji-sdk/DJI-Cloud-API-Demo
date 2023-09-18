package com.dji.sample.manage.model.receiver;

import com.dji.sdk.cloudapi.device.OsdDockDrone;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
public abstract class BasicDeviceProperty {

    public boolean valid() {
        return false;
    }

    public boolean canPublish(OsdDockDrone osd) {
        return valid();
    }
}
