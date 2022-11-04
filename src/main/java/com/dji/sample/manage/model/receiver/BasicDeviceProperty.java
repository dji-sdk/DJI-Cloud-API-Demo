package com.dji.sample.manage.model.receiver;

import lombok.Data;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
@Data
public class BasicDeviceProperty {

    public boolean valid() {
        return false;
    }

    public boolean canPublish(String fieldName, OsdSubDeviceReceiver osd) {
        return true;
    }
}
