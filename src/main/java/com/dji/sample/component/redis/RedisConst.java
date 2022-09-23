package com.dji.sample.component.redis;

import com.dji.sample.manage.model.enums.DeviceDomainEnum;

/**
 * @author sean
 * @version 1.0
 * @date 2022/4/21
 */
public final class RedisConst {

    private RedisConst() {

    }

    public static final String DELIMITER = ":";

    public static final Integer DEVICE_ALIVE_SECOND = 60;

    public static final Integer WEBSOCKET_ALIVE_SECOND = 60 * 60 * 24;

    public static final String ONLINE_PREFIX = "online" + DELIMITER;

    public static final String DEVICE_ONLINE_PREFIX = ONLINE_PREFIX + DeviceDomainEnum.SUB_DEVICE + DELIMITER;

    public static final String WEBSOCKET_PREFIX = "webSocket" + DELIMITER;

    public static final String WEBSOCKET_ALL = WEBSOCKET_PREFIX + "all";

    public static final String HMS_PREFIX = "hms" + DELIMITER;

    public static final String FIRMWARE_UPGRADING_PREFIX = "upgrading" + DELIMITER;

    public static final String STATE_PAYLOAD_PREFIX = "payload" + DELIMITER;

    public static final String LOGS_FILE_PREFIX = "logs_file" + DELIMITER;
}
