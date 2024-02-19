package com.zhao.micro.service;

import com.zhao.micro.entity.Domain;

public interface DomainService extends BaseService<Domain> {

    /**
     * 更新状态
     *
     * @param id     域名id
     * @param status 状态
     * @return
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 随机获取一个域名
     *
     * @return
     */
    Domain getRandom(Integer type);

    /**
     * 初始化缓存
     */
    void initCache();
}
