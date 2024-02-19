package com.zhao.micro.service;

import com.zhao.micro.entity.User;
import com.zhao.micro.model.UserModel;
import com.zhao.micro.reqvo.UserReqVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends BaseService<User> {

    User login(UserReqVO reqVO);

    /**
     * 注册
     *
     * @param user
     * @return
     */
    boolean register(User user);

    /**
     * 修改密码
     *
     * @param userModel
     * @return
     */
    boolean resetPwd(UserModel userModel);

    /**
     * 查询指定代理下的微商的业绩列表
     *
     * @param proxyId 代理id
     * @return
     */
    List<User> getPerformanceByProxy(Long proxyId);

    /**
     * 更新session中的用户信息
     *
     * @param request
     * @param account 用户账号
     */
    void updateUserSession(HttpServletRequest request, String account);

    /**
     * 生成指定用户的活码内容
     *
     * @param qrcodeId 用户的活码id
     * @return
     */
    String generateAliveQrcode(Long qrcodeId, Long userId);

    String generateShareQrcode(int type, Long value, Long userId);

    String generateNewRandomCacheKey(int type, Long value, Long userId);

    /**
     * 生成产品推广码
     *
     * @param productId 产品id
     * @return
     */
    String generateProdPopularizeQrcode(Long productId, Long userId);

    /**
     * 通过手机号查找客户信息，如果没有则创建
     *
     * @param phone  手机号
     * @param name   可会名称，可为空，不作查询的依据
     * @param areaId 地区id
     * @param addr   详细地址
     * @return
     */
    User getCustomerByPhone(String phone, String name, Long areaId, String addr);

    /**
     * 查询代理销售额统计排行
     *
     * @param start 开始条数, 分页
     * @param end   结束条数, 分页
     * @param st    时间段, 开始时间
     * @param et    时间段, 结束时间
     * @return
     */
    List<User> getOrderStatistics(int start, int end, String st, String et);

}
