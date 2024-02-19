package com.zhao.micro.service;

import com.zhao.micro.entity.LoginLog;
import com.zhao.micro.entity.User;
import com.zhao.micro.reqvo.UserReqVO;

public interface LoginLogService extends BaseService<LoginLog> {

    void saveLog(UserReqVO reqVO, User user);

}
