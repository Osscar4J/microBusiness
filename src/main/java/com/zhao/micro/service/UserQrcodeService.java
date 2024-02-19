package com.zhao.micro.service;

import com.zhao.micro.entity.UserQrcode;

import java.util.List;

public interface UserQrcodeService extends BaseService<UserQrcode> {

    List<UserQrcode> getByUserId(Long userId);
}
