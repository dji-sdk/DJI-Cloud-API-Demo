package com.dji.sample.manage.model.receiver;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
public abstract class BasicDeviceProperty {

    public boolean valid() {
        return false;
    }

    public boolean canPublish(String fieldName, OsdSubDeviceReceiver osd) {
        return valid();
    }
}
