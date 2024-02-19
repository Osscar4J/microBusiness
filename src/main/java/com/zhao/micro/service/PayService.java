package com.zhao.micro.service;

import com.zhao.micro.model.OrderModel;
import com.zhao.micro.model.PayModel;
import com.zhao.micro.respvo.WxOrderRespVO;

import java.util.Map;

public interface PayService {
    final String SUCCESS = "SUCCESS";
    final String FAIL = "FAIL";

    /**
     * 确认订单，返回确认订单页面地址（该页面发起支付）
     *
     * @param model
     * @return
     */
    String productOrder(OrderModel model);

    /**
     * 统一下单接口
     *
     * @param prodName  商品名称
     * @param orderNo   订单号
     * @param totalFee  总金额, 单位: 分
     * @param tradeType 交易类型(小程序支付、网页支付、APP支付等)
     * @param prodId    商品id， 交易类型为NATIVE时必填
     * @param ip        用户的ip
     * @param openid    支付方式为JSAPI时必填
     * @param params    额外的参数
     * @return
     */
    WxOrderRespVO unifiedorder(
            String prodName,
            String orderNo,
            int totalFee,
            String tradeType,
            Long prodId,
            String ip,
            String openid,
            Map<String, String> params);

    /**
     * 微信支付回调
     *
     * @param params 回调参数
     */
    String payResultNotify(Map<String, String> params);

    /**
     * 查询订单
     *
     * @param outTradeNo 商户订单号
     * @return
     */
    Map<String, String> orderQuery(String outTradeNo);

    /**
     * 退款查询
     *
     * @param outTradeNo 商户订单号
     * @return
     */
    Map<String, String> refundQuery(String outTradeNo);

    /**
     * 退款
     *
     * @param orderNo   订单号
     * @param reason    退款原因
     * @param totalFee  订单金额
     * @param refundFee 退款金额
     * @return
     */
    Map<String, String> refund(String orderNo, String refundNo, int totalFee, int refundFee, String reason);

    /**
     * 关闭订单
     *
     * @param outTradeNo 订单号
     * @return
     */
    Map<String, String> closeOrder(String outTradeNo);

    /**
     * 下载对账单
     *
     * @param date 日期,格式: 20190422
     * @return
     */
    Map<String, String> downloadbill(String date);

    String getAppid();

    String getAppSecret();

    /**
     * 更新支付密钥
     *
     * @param model
     */
    void updateAppidSecret(PayModel model);

    void initAppidSecret();
}
