package com.zhao.micro.service;

import java.util.List;

public interface CacheService {

    /**
     * 设置缓存
     *
     * @param key
     * @param obj
     * @param seconds 有效时间，单位：秒
     */
    public void put(String key, Object obj, int seconds);

    /**
     * 设置缓存，永不过期
     *
     * @param key
     * @param obj
     */
    public void put(String key, Object obj);

    public Object get(String key);

    public Boolean remove(String key);

    /**
     * 更新有效时长
     *
     * @param *key     key
     * @param *seconds 新的有效时长，单位：秒
     * @return
     */
    public boolean updateExpired(String key, int seconds);

    /**
     * 通过key列表来获取对象列表
     *
     * @param keys 键列表
     * @return
     */
    public List<Object> getByKeys(List<String> keys);

}
