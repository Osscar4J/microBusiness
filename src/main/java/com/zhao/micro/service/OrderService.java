package com.zhao.micro.service;

import com.zhao.micro.entity.Order;
import com.zhao.micro.model.AmountModel;
import com.zhao.micro.model.OrderCountModel;

import java.util.Date;
import java.util.List;

public interface OrderService extends BaseService<Order> {

    /**
     * 生成唯一订单号，长度20位
     *
     * @return
     */
    String generateOrderNo();

    /**
     * 查询指定状态的订单总额
     *
     * @param status   订单状态
     * @param proxyId  代理id，可以为空
     * @param sellerId 微商id，可以为空
     * @return
     */
    Long getSumByStatus(Integer status, Long proxyId, Long sellerId);

    /**
     * 查询两个日期间的订单已支付的每天的金额
     *
     * @param st 开始日期
     * @param et 结束日期
     * @return
     */
    List<AmountModel> getAmountList(Date st, Date et, Long proxyId);

    /**
     * 查询指定用户的订单数量统计（订单总数、已发货、未发货）
     *
     * @param userId 用户id
     * @return
     */
    OrderCountModel getCountByUserId(Long userId);

    Integer queryOrder(String orderNo);

    /**
     * 退款
     *
     * @param orderNo 订单号
     * @param reason  退款原因
     * @return
     */
    boolean refund(String orderNo, String reason);

    void autoQueryOrderStatus(Date date);

}
