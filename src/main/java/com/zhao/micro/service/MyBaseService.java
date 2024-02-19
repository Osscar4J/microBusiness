package com.zhao.micro.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhao.micro.mapper.MyBaseMapper;
import com.zhao.micro.reqvo.BaseReqVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Calendar;

public abstract class MyBaseService<M extends MyBaseMapper, T> extends ServiceImpl<MyBaseMapper<T>, T> implements BaseService<T> {

    @Autowired
    private M myBaseMapper;

    @Override
    public IPage<T> getPage() {
        return getPage(null);
    }

    @Override
    public T getDetail(BaseReqVO reqvo) {
        return (T) myBaseMapper.selectDetail(reqvo);
    }

    @Override
    public IPage<T> getPage(BaseReqVO reqVO) {
        if (reqVO == null)
            reqVO = new BaseReqVO();
        Page<T> page;
        if (reqVO.getNeedPage() == 1) {
            page = new Page<>(reqVO.getCurrent(), reqVO.getSize());
            return myBaseMapper.selectPage(page, reqVO);
        } else {
            page = new Page<>();
            page.setRecords(myBaseMapper.selectPage(reqVO));
            return page;
        }
    }

    private T autoSetCreateTime(Class<?> claz, T entity) {
        try {
            Field createTimefield = claz.getDeclaredField("createTime");
            createTimefield.setAccessible(true);
            createTimefield.set(entity, Calendar.getInstance().getTime());
        } catch (Exception e) {
            if (claz.getSuperclass() != Object.class)
                return autoSetCreateTime(claz.getSuperclass(), entity);
        }
        return entity;
    }


    @Override
    public boolean save(T entity) {
        autoSetCreateTime(entity.getClass(), entity);
        return super.save(entity);
    }

    private T autoSetUpdateTime(Class<?> claz, T entity) {
        try {
            Field createTimefield = claz.getDeclaredField("updateTime");
            createTimefield.setAccessible(true);
            if (createTimefield.get(entity) == null) {
                createTimefield.set(entity, Calendar.getInstance().getTime());
            }
        } catch (Exception e) {
            if (claz.getSuperclass() != Object.class)
                return autoSetUpdateTime(claz.getSuperclass(), entity);
        }
        return entity;
    }

    @Override
    public boolean updateById(T entity) {
        autoSetUpdateTime(entity.getClass(), entity);
        return super.updateById(entity);
    }
}
