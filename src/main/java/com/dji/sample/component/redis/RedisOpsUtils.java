package com.dji.sample.component.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author sean
 * @version 1.0
 * @date 2022/4/19
 */
@Component
public class RedisOpsUtils {

    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisOpsUtils.redisTemplate = redisTemplate;
    }

    /**
     * HSET
     * @param key
     * @param field
     * @param value
     */
    public static void hashSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * HGET
     * @param key
     * @param field
     * @return
     */
    public static Object hashGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * HKEYS
     * @param key
     * @return
     */
    public static Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * HEXISTS
     * @param key
     * @param field
     * @return
     */
    public static boolean hashCheck(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * HDEL
     * @param key
     * @param fields
     * @return
     */
    public static boolean hashDel(String key, Object[] fields) {
        return redisTemplate.opsForHash().delete(key, fields) > 0;
    }

    /**
     * HLEN
     * @param key
     * @return
     */
    public static long hashLen(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * EXPIRE
     * @param key
     * @param timeout
     * @return
     */
    public static boolean expireKey(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * SET
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * GET
     * @param key
     * @return
     */
    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * SETEX
     * @param key
     * @param value
     * @param expire
     */
    public static void setWithExpire(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * TTL
     * @param key
     * @return
     */
    public static long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * EXISTS
     * @param key
     * @return
     */
    public static boolean checkExist(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * DEL
     * @param key
     * @return
     */
    public static boolean del(String key) {
        return RedisOpsUtils.checkExist(key) && redisTemplate.delete(key);
    }

    /**
     * KEYS
     * @param pattern
     * @return
     */
    public static Set<String> getAllKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * RPUSH
     * @param key
     * @param value
     */
    public static void listRPush(String key, Object... value) {
        if (value.length == 0) {
            return;
        }
        for (Object val : value) {
            redisTemplate.opsForList().rightPush(key, val);
        }
    }

    /**
     * LRANGE
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static List<Object> listGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * LRANGE
     * @param key
     * @return
     */
    public static List<Object> listGetAll(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * LLen
     * @param key
     * @return
     */
    public static Long listLen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * ZADD
     * @param key
     * @param value
     * @param score
     */
    public static Boolean zAdd(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * ZREM
     * @param key
     * @param value
     */
    public static Boolean zRemove(String key, Object... value) {
        return redisTemplate.opsForZSet().remove(key, value) > 0;
    }
    /**
     * ZRANGE
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set<Object> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * ZRANGE
     * @param key
     * @return
     */
    public static Object zGetMin(String key) {
        Set<Object> objects = zRange(key, 0, 0);
        if (CollectionUtils.isEmpty(objects)) {
            return null;
        }
        return objects.iterator().next();
    }

    /**
     * ZSCORE
     * @param key
     * @param value
     * @return
     */
    public static Double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * ZINCRBY
     * @param key
     * @param value
     * @param delta
     */
    public static Double zIncrement(String key, Object value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }
}
