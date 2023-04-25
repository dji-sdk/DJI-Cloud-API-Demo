package com.dji.sample.control.model.dto;

import com.dji.sample.common.util.SpringBeanUtils;
import com.dji.sample.control.service.impl.RemoteDebugHandler;
import com.dji.sample.manage.model.enums.DockModeCodeEnum;
import com.dji.sample.manage.service.IDeviceService;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sean
 * @version 1.4
 * @date 2023/4/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RemoteDebugOpenState extends RemoteDebugHandler {

    @Override
    public boolean canPublish(String sn) {
        IDeviceService deviceService = SpringBeanUtils.getBean(IDeviceService.class);
        DockModeCodeEnum dockMode = deviceService.getDockMode(sn);
        return DockModeCodeEnum.IDLE == dockMode;
    }
}
