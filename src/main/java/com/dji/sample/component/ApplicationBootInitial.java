package com.dji.sample.component;

import com.dji.sample.manage.model.DeviceStatusManager;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.model.param.DeviceQueryParam;
import com.dji.sample.manage.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author sean.zhou
 * @date 2021/11/24
 * @version 0.1
 */
@Component
public class ApplicationBootInitial implements CommandLineRunner {

    @Autowired
    private IDeviceService deviceService;

    /**
     * Subscribe to the devices that exist in the database when the program starts,
     * to prevent the data from being different from the pilot side due to program interruptions.
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        deviceService.getDevicesByParams(DeviceQueryParam.builder().build())
                .forEach(device -> {
                    deviceService.subscribeTopicOnline(device.getDeviceSn());
                    DeviceStatusManager.STATUS_MANAGER.put(
                            DeviceDomainEnum.getVal(device.getDomain()) + "/"
                                    + device.getDeviceSn(), LocalDateTime.now());
                });
    }
}