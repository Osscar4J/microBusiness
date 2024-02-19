package com.zhao.micro.service;

import com.zhao.micro.entity.Area;

import java.util.List;

public interface AreaService extends BaseService<Area> {

    /**
     * 随机获取城市列表
     *
     * @param count 要获取的数量
     * @return
     */
    List<Area> getRandCidy(int count);
}
