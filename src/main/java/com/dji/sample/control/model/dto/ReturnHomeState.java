package com.dji.sample.control.model.dto;

import com.dji.sample.common.util.SpringBeanUtils;
import com.dji.sample.control.service.impl.RemoteDebugHandler;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.receiver.OsdSubDeviceReceiver;
import com.dji.sample.manage.service.IDeviceRedisService;

/**
 * @author sean
 * @version 1.4
 * @date 2023/4/19
 */

public class ReturnHomeState extends RemoteDebugHandler {

    @Override
    public boolean canPublish(String sn) {
        IDeviceRedisService deviceRedisService = SpringBeanUtils.getBean(IDeviceRedisService.class);
        return deviceRedisService.getDeviceOnline(sn)
                .map(DeviceDTO::getChildDeviceSn)
                .flatMap(deviceSn -> deviceRedisService.getDeviceOsd(deviceSn, OsdSubDeviceReceiver.class))
                .map(OsdSubDeviceReceiver::getElevation)
                .map(elevation -> elevation > 0)
                .orElse(false);
    }
}
