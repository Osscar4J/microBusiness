package com.zhao.micro.service.impl;

import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.entity.UserQrcode;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.mapper.UserQrcodeMapper;
import com.zhao.micro.service.MyBaseService;
import com.zhao.micro.service.UserQrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQrcodeServiceImpl extends MyBaseService<UserQrcodeMapper, UserQrcode> implements UserQrcodeService {

    @Autowired
    private UserQrcodeMapper userQrcodeMapper;

    @Override
    public List<UserQrcode> getByUserId(Long userId) {
        if (userId == null)
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        return userQrcodeMapper.selectByUserId(userId);
    }
}
