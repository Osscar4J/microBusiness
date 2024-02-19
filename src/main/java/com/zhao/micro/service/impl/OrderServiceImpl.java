package com.zhao.micro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.Order;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.mapper.OrderMapper;
import com.zhao.micro.model.AmountModel;
import com.zhao.micro.model.OrderCountModel;
import com.zhao.micro.service.MyBaseService;
import com.zhao.micro.service.OrderService;
import com.zhao.micro.service.PayService;
import com.zhao.micro.utils.DateStyle;
import com.zhao.micro.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl extends MyBaseService<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Lazy
//    @Qualifier("xorpayService")
    @Qualifier("wxpayService")
    @Autowired
    private PayService payService;
    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public String generateOrderNo() {
        String orderNo;
        do {
            orderNo = DateUtils.DateToString(new Date(), DateStyle.yyyyMMddHHmmss)
                    + (System.currentTimeMillis() + "").substring(10, 13) + (System.nanoTime() + "").substring(7, 10);
        } while (this.getById(orderNo) != null);
        return orderNo;
    }

    @Override
    public Long getSumByStatus(Integer status, Long proxyId, Long sellerId) {
        Long amount = orderMapper.selectSumByStatus(status, proxyId, sellerId);
        if (amount == null)
            return 0L;
        return amount;
    }

    @Override
    public List<AmountModel> getAmountList(Date st, Date et, Long proxyId) {
        if (st == null || et == null)
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        return orderMapper.selectAmount(st, et, proxyId);
    }

    @Override
    public OrderCountModel getCountByUserId(Long userId) {
        OrderCountModel re = new OrderCountModel();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("seller_id", userId);
        wrapper.eq("status", SysConstants.OrderStatus.PAYED);
        wrapper.eq("ems_id", -1);
        re.setNems(this.count(wrapper));
        wrapper = new QueryWrapper();
        wrapper.eq("seller_id", userId);
        wrapper.ne("ems_id", -1);
        re.setEms(this.count(wrapper));
        re.setTotal(re.getEms() + re.getNems());
        return re;
    }

    @Override
    public Integer queryOrder(String orderNo) {
        Map<String, String> map = payService.orderQuery(orderNo);
        String orderStatus = map.get("status");
        if ("success".equalsIgnoreCase(orderStatus)
                || "payed".equalsIgnoreCase(orderStatus)) {
            map.put("orderNo", orderNo);
            payService.payResultNotify(map);
            return SysConstants.OrderStatus.PAYED;
        }
        throw new BusinessException(500, orderStatus);
    }

    private String createRefundNo() {
        String res = this.generateOrderNo();
        return "RF" + res.substring(2);
    }

    @Override
    public boolean refund(String orderNo, String reason) {
        Order order;
        synchronized (this) {
            order = this.getById(orderNo);
            if (order == null)
                throw new BusinessException(ResponseStatus.ORDER_INVALID);
            // 生成退款单号
            if (order.getRefundNo() == null) {
                order.setRefundNo(this.createRefundNo());
                UpdateWrapper wrapper = new UpdateWrapper();
                wrapper.eq("order_no", order.getOrderNo());
                wrapper.set("refund_no", order.getRefundNo());
                wrapper.set("update_time", new Date());
                this.update(wrapper);
            }
        }
        Map<String, String> result = payService.refund(
                order.getTransationNo(),
                order.getRefundNo(),
                order.getTotalFee(),
                order.getTotalFee(),
                reason);
        if ("SUCCESS".equalsIgnoreCase(result.get("result_code"))) {
            order.setStatus(SysConstants.OrderStatus.REFUNDED);
            order.setRefundTime(new Date());
            order.setReason(reason);
            return this.updateById(order);
        } else {
            logger.error("error: {}", result);
            throw new BusinessException(500, result.get("err_code_des"));
        }
    }

    @Async
    @Override
    public void autoQueryOrderStatus(Date date) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.ge("create_time", date);
        wrapper.eq("status", SysConstants.OrderStatus.NOT_PAY);
        List<Order> orders = this.list(wrapper);
        if (!orders.isEmpty()) {
            orders.forEach(order -> this.queryOrder(order.getOrderNo()));
            logger.info("查询未支付订单状态数量：{}", orders.size());
        }
    }
}
