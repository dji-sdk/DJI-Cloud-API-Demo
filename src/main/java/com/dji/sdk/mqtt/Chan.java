package com.dji.sdk.mqtt;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

/**
 * The demo is only for functional closure, which is not recommended.
 * @author sean.zhou
 * @date 2021/11/22
 * @version 0.1
 */
public class Chan {

    private static final ConcurrentHashMap<String, Chan> CHANNEL = new ConcurrentHashMap<>();

    private static final int UNIT = 1000_000;

    private volatile CommonTopicResponse data;

    private volatile Thread t;

    private Chan () {

    }

    public static Chan getInstance(String tid, boolean isNeedCreate) {
        if (!isNeedCreate) {
            return CHANNEL.get(tid);
        }
        Chan chan = new Chan();
        CHANNEL.put(tid, chan);
        return chan;
    }

    public CommonTopicResponse get(String tid, long timeout) {
        Chan chan = CHANNEL.get(tid);
        if (Objects.isNull(chan)) {
            return null;
        }
        chan.t = Thread.currentThread();
        LockSupport.parkNanos(chan.t, timeout * UNIT);
        chan.t = null;
        CHANNEL.remove(tid);
        return chan.data;
    }

    public void put(CommonTopicResponse response) {
        Chan chan = CHANNEL.get(response.getTid());
        if (Objects.isNull(chan)) {
            return;
        }
        chan.data = response;
        if (chan.t == null) {
            return;
        }
        LockSupport.unpark(chan.t);
    }
}