package com.dji.sample.component;

import com.dji.sample.component.mqtt.service.IMqttTopicService;
import com.dji.sample.component.redis.RedisConst;
import com.dji.sample.component.redis.RedisOpsUtils;
import com.dji.sample.manage.model.dto.DeviceDTO;
import com.dji.sample.manage.model.enums.DeviceDomainEnum;
import com.dji.sample.manage.service.IDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisOpsUtils redisOps;

    @Autowired
    private IMqttTopicService topicService;

    /**
     * Check the status of the devices every 30 seconds. It is recommended to use cache.
     */
    @Scheduled(initialDelay = 30, fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    private void deviceStatusListen() {
        int start = RedisConst.DEVICE_ONLINE_PREFIX.length();

        redisOps.getAllKeys(RedisConst.DEVICE_ONLINE_PREFIX + "*").forEach(key -> {
            long expire = redisOps.getExpire(key);
            if (expire <= 30) {
                DeviceDTO device = (DeviceDTO) redisOps.get(key);
                if (device.getDomain().equals(DeviceDomainEnum.SUB_DEVICE.getDesc())) {
                    deviceService.subDeviceOffline(key.substring(start));
                } else {
                    deviceService.unsubscribeTopicOffline(key.substring(start));
                    deviceService.pushDeviceOfflineTopo(device.getWorkspaceId(), device.getDeviceSn());
                }
                redisOps.del(key);
            }
        });

        log.info("Subscriptions: {}", Arrays.toString(topicService.getSubscribedTopic()));
    }

}