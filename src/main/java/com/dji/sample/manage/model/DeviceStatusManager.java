package com.dji.sample.manage.model;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The demo is only for functional closure, which is not recommended,
 * and it is recommended to use caching for handling.
 * @author sean.zhou
 * @version 0.1
 * @date 2021/11/25
 */
public class DeviceStatusManager {

    public static final ConcurrentHashMap<String, LocalDateTime> STATUS_MANAGER =
            new ConcurrentHashMap<>(16);

    public static final Integer DEFAULT_ALIVE_SECOND = 30;

}