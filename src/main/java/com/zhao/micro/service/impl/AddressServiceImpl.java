package com.zhao.micro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.Address;
import com.zhao.micro.mapper.AddressMapper;
import com.zhao.micro.service.AddressService;
import com.zhao.micro.service.MyBaseService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends MyBaseService<AddressMapper, Address> implements AddressService {

    @Override
    public Address getByUserId(Long userId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id", userId);
        wrapper.ne("status", SysConstants.DELETED);
        return this.getOne(wrapper);
    }
}
