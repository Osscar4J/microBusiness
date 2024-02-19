package com.zhao.micro.mapper;

import com.zhao.micro.entity.LoginLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginLogMapper extends MyBaseMapper<LoginLog> {

    @Select("SELECT * FROM login_log_tb WHERE DATE_FORMAT(create_time, '%Y-%m-%d')=#{date} and user_id=#{userId}")
    List<LoginLog> selectByDay(@Param("date") String date, @Param("userId") Long userId);
}
