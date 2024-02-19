package com.zhao.micro.service.impl;

import com.zhao.micro.entity.Area;
import com.zhao.micro.mapper.AreaMapper;
import com.zhao.micro.service.AreaService;
import com.zhao.micro.service.MyBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl extends MyBaseService<AreaMapper, Area> implements AreaService {

    @Autowired
    private AreaMapper areaMapper;

    @Override
    public List<Area> getRandCidy(int count) {
        return areaMapper.selectRandCity(count);
    }
}
