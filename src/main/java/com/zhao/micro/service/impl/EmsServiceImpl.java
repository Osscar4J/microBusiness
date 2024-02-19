package com.zhao.micro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhao.micro.entity.Ems;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.mapper.EmsMapper;
import com.zhao.micro.service.EmsService;
import com.zhao.micro.service.MyBaseService;
import com.zhao.micro.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmsServiceImpl extends MyBaseService<EmsMapper, Ems> implements EmsService {

    @Lazy
    @Autowired
    private OrderService orderService;

    @Transactional
    @Override
    public boolean save(Ems entity) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("ems_no", entity.getEmsNo());
        synchronized (this) {
            if (this.getOne(wrapper) != null)
                throw new BusinessException(500, "已保存");
            if (super.save(entity)) {
                updateOrderEmsNo(entity.getOrderNo(), entity.getId());
                return true;
            }
        }
        return false;
    }

    private boolean updateOrderEmsNo(String orderNo, Long emsId) {
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("order_no", orderNo);
        wrapper.set("ems_id", emsId);
        return orderService.update(wrapper);
    }
}
