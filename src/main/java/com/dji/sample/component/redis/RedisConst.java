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

    public static final Integer DEVICE_ALIVE_SECOND = 60;

    public static final Integer WEBSOCKET_ALIVE_SECOND = 60 * 60 * 24;

    public static final String ONLINE_PREFIX = "online:";

    public static final String DEVICE_ONLINE_PREFIX = ONLINE_PREFIX + DeviceDomainEnum.SUB_DEVICE + ":";

    public static final String WEBSOCKET_PREFIX = "webSocket:";

    public static final String WEBSOCKET_ALL = "webSocket:all";

    public static final String HMS_PREFIX = "hms:";
}
