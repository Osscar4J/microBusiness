package com.zhao.micro.service.impl;

import com.zhao.micro.entity.LoginLog;
import com.zhao.micro.entity.User;
import com.zhao.micro.mapper.LoginLogMapper;
import com.zhao.micro.reqvo.UserReqVO;
import com.zhao.micro.service.LoginLogService;
import com.zhao.micro.service.MyBaseService;
import com.zhao.micro.service.UserService;
import com.zhao.micro.utils.DateStyle;
import com.zhao.micro.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class LoginLogServiceImpl extends MyBaseService<LoginLogMapper, LoginLog> implements LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;
    @Autowired
    private UserService userService;

    @Async
    @Override
    public void saveLog(UserReqVO reqVO, User user) {
        LoginLog log = new LoginLog();
        log.setClient(reqVO.getClient());
        log.setIp(reqVO.getIp());
        log.setUserId(user.getId());
        // 增加连续登录天数
        Calendar calendar = Calendar.getInstance();
        List<LoginLog> loginLogs = loginLogMapper.selectByDay(DateUtils.DateToString(calendar.getTime(), DateStyle.YYYY_MM_DD), user.getId());
        // 当天只有第一次登陆才增加登陆天数
        if (loginLogs.isEmpty()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
            loginLogs = loginLogMapper.selectByDay(DateUtils.DateToString(calendar.getTime(), DateStyle.YYYY_MM_DD), user.getId());
            if (loginLogs.isEmpty()) {
                user.setLoginDays(1);
            } else {
                user.setLoginDays(user.getLoginDays() + 1);
            }
            if (user.getLoginDays() > user.getMaxLoginDays())
                user.setMaxLoginDays(user.getLoginDays());
        }
        user.setLastLoginTime(new Date());
        userService.updateById(user);
        this.save(log);
    }
}
