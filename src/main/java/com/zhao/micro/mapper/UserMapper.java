package com.zhao.micro.mapper;

import com.zhao.micro.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserMapper extends MyBaseMapper<User> {


    /**
     * 账号+密码查询用户
     *
     * @param account 账号
     * @param passwd  密码
     * @return
     */
    User selectByUsernamePasswd(@Param("account") String account, @Param("passwd") String passwd);

    /**
     * 给指定用户的下线数量+1
     *
     * @param id 用户id
     * @return
     */
    @Update("update user_tb set proxy_count=proxy_count+1 where id=#{id}")
    int addOneProxyCount(@Param("id") Long id);

    /**
     * 给指定用户的下线数量-1
     *
     * @param id 用户id
     * @return
     */
    @Update("update user_tb set proxy_count=proxy_count-1 where id=#{id}")
    int minusOneProxyCount(@Param("id") Long id);

    /**
     * 查询指定代理下的微商的业绩列表
     *
     * @param proxyId  代理id
     * @param today    今天的日期，格式：yyyy-MM-dd
     * @param yestoday 昨天的日期，格式: yyyy-MM-dd
     * @param weekSt   上周开始日期
     * @param weekEt   上周结束日期
     * @param monthSt  本月开始日期
     * @param monthEt  本月结束日期
     * @return
     */
    List<User> selectPerformance(
            @Param("proxyId") Long proxyId,
            @Param("today") String today,
            @Param("yestoday") String yestoday,
            @Param("weekSt") Date weekSt,
            @Param("weekEt") Date weekEt,
            @Param("monthSt") Date monthSt,
            @Param("monthEt") Date monthEt);

    User selectByPhoneWithAddr(@Param("phone") String userId);

    /**
     * 查询代理销售额统计排行
     *
     * @param start 开始条数, 分页
     * @param end   结束条数, 分页
     * @param st    时间段, 开始时间
     * @param et    时间段, 结束时间
     * @return
     */
    List<User> selectOrderStatistics(@Param("start") int start, @Param("end") int end, @Param("st") String st, @Param("et") String et);
}
