package com.zhao.micro.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayUtil;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.Order;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.mapper.ProductMapper;
import com.zhao.micro.pay.wx.MyWXPayConfig;
import com.zhao.micro.pay.wx.WXPay;
import com.zhao.micro.respvo.WxOrderRespVO;
import com.zhao.micro.service.OrderService;
import com.zhao.micro.service.PayService;
import com.zhao.micro.service.ProdPropertyLinkService;
import com.zhao.micro.service.SmsService;
import com.zhao.micro.utils.DateStyle;
import com.zhao.micro.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("wxpayService")
public class WXPayServiceImpl extends AbstractPayService {

    @Value("${wxpay-notifyurl}")
    private String notifyUrl;
    @Value("${order-confirm-url}")
    private String confirmOrderUrl;
    private Logger logger = LoggerFactory.getLogger(WXPayServiceImpl.class);
    @Lazy
    @Autowired
    private OrderService orderService;
    @Lazy
    @Autowired
    private SmsService smsService;
    @Autowired
    private ProdPropertyLinkService prodPropertyLinkService;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public WxOrderRespVO unifiedorder(String prodName, String orderNo, int totalFee, String tradeType, Long prodId, String ip, String openid, Map<String, String> params) {
        try {
            if (StringUtils.isEmpty(ip))
                ip = "192.168.31.177";
            MyWXPayConfig config = new MyWXPayConfig();
            WXPay wxpay = new WXPay(config);
            Map<String, String> data = new HashMap<>();
//            data.put("appid", SysConstants.GH_APPID);
//            data.put("mch_id", MyWXPayConfig.MCH_ID);
            data.put("body", prodName);
            data.put("out_trade_no", orderNo);
            data.put("openid", openid);
//            data.put("device_info", "");
            data.put("fee_type", "CNY");
            data.put("total_fee", String.valueOf(totalFee));
            data.put("spbill_create_ip", ip);
            data.put("notify_url", notifyUrl);
            data.put("trade_type", tradeType.toUpperCase());
            data.put("sign_type", MyWXPayConfig.SignType_MD5);
            if (prodId != null)
                data.put("product_id", String.valueOf(prodId)); // trade_type是NATIVE的时候此参数必填
            // 订单15分钟后失效
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 15);
            data.put("time_expire", DateUtils.DateToString(calendar.getTime(), DateStyle.yyyyMMddHHmmss));
            // 追加额外的参数
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet())
                    data.put(key, params.get(key));
            }
            Map<String, String> resp = wxpay.unifiedOrder(data);

            WxOrderRespVO respVO = new WxOrderRespVO();
            respVO.setOrderNo(orderNo);
            respVO.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));

            if (SUCCESS.equalsIgnoreCase(resp.get("result_code"))) {
                if (SysConstants.WXPayTradeType.JSAPI.equalsIgnoreCase(tradeType)) { // 微信小程序、公众号支付
                    JSONObject json = new JSONObject();
                    for (String key : resp.keySet())
                        json.put(key, resp.get(key));

                    respVO.setNonceStr(json.getString("nonce_str"));

                    Map<String, String> payMap = new HashMap<>();
                    payMap.put("appId", SysConstants.GH_APPID);
                    payMap.put("timeStamp", respVO.getTimeStamp());
                    payMap.put("nonceStr", json.getString("nonce_str"));
                    payMap.put("signType", "MD5");
                    payMap.put("package", "prepay_id=" + json.get("prepay_id"));
                    respVO.setPaySign(WXPayUtil.generateSignature(payMap, MyWXPayConfig.API_SECRET));

                    respVO.setPayStr(json.toJSONString());
                    return respVO;
                } else { // 其他
                    respVO.setCodeUrl(resp.get("code_url"));
                    return respVO;
                }
            } else {
                String msg = resp.get("err_code_des");
                if (msg == null)
                    msg = resp.get("return_msg");
                throw new BusinessException(500, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(500, e.getLocalizedMessage());
        }
    }

    @Override
    public String payResultNotify(Map<String, String> params) {
        String orderNo = params.get("out_trade_no"); // 订单号
        String transactionId = params.get("transaction_id"); // 微信订单号
        String resultCode = params.get("result_code"); // 业务结果，SUCCESS/FAIL
        synchronized (this) {
            Order order = orderService.getById(orderNo);
            if (order == null
                    || order.getStatus() == SysConstants.OrderStatus.PAYED
                    || order.getStatus() == SysConstants.OrderStatus.REFUNDED)
                return PayService.SUCCESS;

            if (SUCCESS.equalsIgnoreCase(resultCode)) { // 支付成功
                order.setStatus(SysConstants.OrderStatus.PAYED);
                order.setTransationNo(transactionId);
                order.setPayTime(new Date());
                // 短信通知客户购买成功
                smsService.sendBuyMsg(orderNo);
                // 更新库存
                prodPropertyLinkService.minusRemainByOrder(orderNo, order.getProdId());
                // 更新销量
                productMapper.addSellCount(order.getProdId(), order.getProdCount());
            } else { // 支付失败
                logger.error("支付失败：" + params);
                order.setStatus(SysConstants.OrderStatus.PAY_FAIL);
            }
            orderService.updateById(order);
        }
        return SUCCESS;
    }

    @Override
    public Map<String, String> orderQuery(String outTradeNo) {
        try {
            WXPay wxpay = new WXPay(new MyWXPayConfig());
            Map<String, String> data = new HashMap<>();
            data.put("out_trade_no", outTradeNo);
            Map<String, String> re = wxpay.orderQuery(data);
            logger.info("订单：{}，查询结果：{}", outTradeNo, re);
            if (re.get("return_code").equalsIgnoreCase(SUCCESS)
                    && re.get("result_code").equalsIgnoreCase(SUCCESS)
                    && re.get("trade_state").equalsIgnoreCase(SUCCESS)) {
                re.put("status", "success");
                Order order = orderService.getById(outTradeNo);
                if (order.getStatus() != SysConstants.OrderStatus.PAYED) {
                    order.setTransationNo(re.get("transaction_id"));
                    order.setPayTime(DateUtils.StringToDate(re.get("time_end")));
//                    order.setStatus(SysConstants.OrderStatus.PAYED);
                    orderService.updateById(order);
                }
            } else {
                throw new BusinessException(500, "支付失败");
            }
            return re;
        } catch (Exception e) {
            logger.error("err:{}",e);
        }
        return null;
    }

    @Override
    public Map<String, String> refundQuery(String outTradeNo) {
        try {
            WXPay wxpay = new WXPay(new MyWXPayConfig());
            Map<String, String> data = new HashMap<>();
            data.put("out_trade_no", outTradeNo);
            return wxpay.refundQuery(data);
        } catch (Exception e) {
            logger.error("error:{}",e);
        }
        return null;
    }

    @Override
    public Map<String, String> refund(String orderNo, String refundNo, int totalFee, int refundFee, String reason) {
        try {
            WXPay wxpay = new WXPay(new MyWXPayConfig());
            Map<String, String> data = new HashMap<>();
            data.put("out_trade_no", orderNo);
//            data.put("transaction_id", orderNo);
            data.put("out_refund_no", refundNo);
            data.put("refund_fee", String.valueOf(refundFee));
            data.put("total_fee", String.valueOf(totalFee));
            if (!StringUtils.isEmpty(reason))
                data.put("refund_desc", reason);
            return wxpay.refund(data);
        } catch (Exception e) {
            logger.error("error:{}", e);
        }
        return null;
    }

    @Override
    public Map<String, String> closeOrder(String outTradeNo) {
        try {
            // 关闭订单
            WXPay wxpay = new WXPay(new MyWXPayConfig());
            Map<String, String> data = new HashMap<>();
            data.put("out_trade_no", outTradeNo);
            return wxpay.closeOrder(data);
        } catch (Exception e) {
            logger.error("error:{}",e);
        }
        return null;
    }

    @Override
    public Map<String, String> downloadbill(String date) {
        try {
            WXPay wxpay = new WXPay(new MyWXPayConfig());
            Map<String, String> data = new HashMap<>();
            data.put("bill_date", date);
            // 对账单类型
            // ALL（默认值），返回当日所有订单信息（不含充值退款订单）
            //SUCCESS，返回当日成功支付的订单（不含充值退款订单）
            //REFUND，返回当日退款订单（不含充值退款订单）
            //RECHARGE_REFUND，返回当日充值退款订单
            data.put("bill_type", "ALL");
            return wxpay.downloadBill(data);
        } catch (Exception e) {
            logger.error("error:{}", e);
        }
        return null;
    }
}
