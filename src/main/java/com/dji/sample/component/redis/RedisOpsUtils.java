package com.dji.sample.component.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * HSET
     * @param key
     * @param field
     * @param value
     */
    public void hashSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * HGET
     * @param key
     * @param field
     * @return
     */
    public Object hashGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * HKEYS
     * @param key
     * @return
     */
    public Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * HEXISTS
     * @param key
     * @param field
     * @return
     */
    public boolean hashCheck(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * HDEL
     * @param key
     * @param fields
     * @return
     */
    public boolean hashDel(String key, Object[] fields) {
        return redisTemplate.opsForHash().delete(key, fields) > 0;
    }

    /**
     * EXPIRE
     * @param key
     * @param timeout
     * @return
     */
    public boolean expireKey(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * SET
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * GET
     * @param key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * SETEX
     * @param key
     * @param value
     * @param expire
     */
    public void setWithExpire(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * TTL
     * @param key
     * @return
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * EXISTS
     * @param key
     * @return
     */
    public boolean checkExist(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * DEL
     * @param key
     * @return
     */
    public boolean del(String key) {
        return this.checkExist(key) && redisTemplate.delete(key);
    }

    /**
     * KEYS
     * @param pattern
     * @return
     */
    public Set<String> getAllKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * RPUSH
     * @param key
     * @param value
     */
    public void listRPush(String key, Object... value) {
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
    public List<Object> listGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * LRANGE
     * @param key
     * @return
     */
    public List<Object> listGetAll(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * LLen
     * @param key
     * @return
     */
    public Long listLen(String key) {
        return redisTemplate.opsForList().size(key);
    }
}
