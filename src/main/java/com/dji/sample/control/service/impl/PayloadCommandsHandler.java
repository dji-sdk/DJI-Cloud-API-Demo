package com.dji.sample.control.service.impl;

import com.dji.sample.common.util.SpringBeanUtils;
import com.dji.sample.control.model.param.DronePayloadParam;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.receiver.OsdCameraReceiver;
import com.dji.sample.manage.model.receiver.OsdSubDeviceReceiver;
import com.dji.sample.manage.service.IDevicePayloadService;
import com.dji.sample.manage.service.IDeviceRedisService;

import java.util.Optional;

/**
 * @author sean
 * @version 1.4
 * @date 2023/4/23
 */
public abstract class PayloadCommandsHandler {

    DronePayloadParam param;

    OsdCameraReceiver osdCamera;

    PayloadCommandsHandler(DronePayloadParam param) {
        this.param = param;
    }

    public boolean valid() {
        return true;
    }

    public boolean canPublish(String deviceSn) {
        Optional<OsdSubDeviceReceiver> deviceOpt = SpringBeanUtils.getBean(IDeviceRedisService.class)
                .getDeviceOsd(deviceSn, OsdSubDeviceReceiver.class);
        if (deviceOpt.isEmpty()) {
            throw new RuntimeException("The device is offline.");
        }
        osdCamera = deviceOpt.get().getCameras().stream()
                .filter(osdCamera -> param.getPayloadIndex().equals(osdCamera.getPayloadIndex()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Did not receive osd information about the camera, please check the cache data."));
        return true;
    }

    private String checkDockOnline(String dockSn) {
        Optional<DeviceDTO> deviceOpt = SpringBeanUtils.getBean(IDeviceRedisService.class).getDeviceOnline(dockSn);
        if (deviceOpt.isEmpty()) {
            throw new RuntimeException("The dock is offline.");
        }
        return deviceOpt.get().getChildDeviceSn();
    }

    private void checkDeviceOnline(String deviceSn) {
        boolean isOnline = SpringBeanUtils.getBean(IDeviceRedisService.class).checkDeviceOnline(deviceSn);
        if (!isOnline) {
            throw new RuntimeException("The device is offline.");
        }
    }

    private void checkAuthority(String deviceSn) {
        boolean hasAuthority = SpringBeanUtils.getBean(IDevicePayloadService.class)
                .checkAuthorityPayload(deviceSn, param.getPayloadIndex());
        if (!hasAuthority) {
            throw new RuntimeException("The device does not have payload control authority.");
        }
    }

    public final void checkCondition(String dockSn) {
        if (!valid()) {
            throw new RuntimeException("illegal argument");
        }

        String deviceSn = checkDockOnline(dockSn);
        checkDeviceOnline(deviceSn);
        checkAuthority(deviceSn);

        if (!canPublish(deviceSn)) {
            throw new RuntimeException("The current state of the drone does not support this function, please try again later.");
        }
    }

}
