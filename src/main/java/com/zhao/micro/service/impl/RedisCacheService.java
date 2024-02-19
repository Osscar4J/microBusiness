package com.zhao.micro.service.impl;

import com.zhao.micro.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService implements CacheService {

    @Autowired
    private RedisTemplate<String, Object> objRedisTemplate;

    @Override
    public void put(String key, Object obj, int seconds) {
        objRedisTemplate.opsForValue().set(key, obj, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void put(String key, Object obj) {
        objRedisTemplate.opsForValue().set(key, obj);
    }

    @Override
    public Object get(String key) {
        return objRedisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean remove(String key) {
        return objRedisTemplate.delete(key);
    }

    @Override
    public boolean updateExpired(String key, int seconds) {
        return objRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    @Override
    public List<Object> getByKeys(List<String> keys) {
        return objRedisTemplate.opsForValue().multiGet(keys);
    }

}
