package com.dji.sample.control.service.impl;

import com.dji.sample.common.util.SpringBeanUtils;
import com.dji.sample.manage.model.enums.DockModeCodeEnum;
import com.dji.sample.manage.service.IDeviceService;

/**
 * @author sean
 * @version 1.3
 * @date 2022/10/27
 */
public class RemoteDebugHandler {

    public boolean valid() {
        return false;
    }

    public boolean canPublish(String sn) {
        IDeviceService deviceService = SpringBeanUtils.getBean(IDeviceService.class);
        DockModeCodeEnum dockMode = deviceService.getDockMode(sn);
        return DockModeCodeEnum.REMOTE_DEBUGGING == dockMode;
    }
}
