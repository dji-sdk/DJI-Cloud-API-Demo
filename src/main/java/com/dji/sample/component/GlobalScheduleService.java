package com.dji.sample.component;

import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.service.IDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.dji.sample.manage.model.DeviceStatusManager.DEFAULT_ALIVE_SECOND;
import static com.dji.sample.manage.model.DeviceStatusManager.STATUS_MANAGER;

/**
 * @author sean.zhou
 * @date 2021/11/24
 * @version 0.1
 */
@Component
@Slf4j
public class GlobalScheduleService {

    @Autowired
    private IDeviceService deviceService;

    /**
     * Check the status of the devices every 30 seconds. It is recommended to use cache.
     */
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    private void deviceStatusListen() {
        for (Map.Entry<String, LocalDateTime> entry : STATUS_MANAGER.entrySet()) {
            if (entry.getValue().isAfter(
                    LocalDateTime.now().minusSeconds(DEFAULT_ALIVE_SECOND))) {
                continue;
            }

            String device = entry.getKey();
            int index = device.indexOf("/");

            STATUS_MANAGER.remove(device);

            int type = Integer.parseInt(device.substring(0, index));
            String sn = device.substring(index + 1);
            // Determine whether it is a gateway device.
            if (DeviceDomainEnum.GATEWAY.getVal() == type) {
                deviceService.deviceOffline(sn);
                deviceService.unsubscribeTopicOffline(sn);
                continue;
            }

            deviceService.subDeviceOffline(sn);
        }
    }

}