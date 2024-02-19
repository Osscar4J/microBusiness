package com.zhao.micro.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.Order;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.mapper.ProductMapper;
import com.zhao.micro.model.PayModel;
import com.zhao.micro.respvo.WxOrderRespVO;
import com.zhao.micro.service.*;
import com.zhao.micro.utils.HttpUtils;
import com.zhao.micro.utils.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("xorpayService")
public class XorPayServiceImpl extends AbstractPayService {

    private Logger logger = LoggerFactory.getLogger(XorPayServiceImpl.class);
    @Value("${wxpay-notifyurl}")
    private String notifyUrl;
    @Value("${xorpay_aid}")
    private String aid;
    @Value("${xorpay_appsecret}")
    private String appsecret;
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
    @Autowired
    private CacheService cacheService;

    @Override
    public String getAppid() {
        return aid;
    }

    @Override
    public String getAppSecret() {
        return appsecret;
    }

    @Override
    public void initAppidSecret() {
        String temp = (String) cacheService.get(SysConstants.REDIS_PAY_APPID);
        if (temp != null)
            this.aid = temp;
        temp = (String) cacheService.get(SysConstants.REDIS_PAY_APPSECRET);
        if (temp != null)
            this.appsecret = temp;
    }

    @Override
    public void updateAppidSecret(PayModel model) {
        this.aid = model.getAppid();
        this.appsecret = model.getSecret();
        cacheService.put(SysConstants.REDIS_PAY_APPID, aid);
        cacheService.put(SysConstants.REDIS_PAY_APPID, appsecret);
    }

    @Override
    public WxOrderRespVO unifiedorder(String prodName, String orderNo, int totalFee, String tradeType, Long prodId, String ip, String openid, Map<String, String> params) {
        String sign = MD5Utils.generate(prodName + tradeType + totalFee / 100.0 + orderNo + notifyUrl + appsecret);
        Map<String, String> map = new HashMap<>();
        map.put("name", prodName);
        map.put("pay_type", tradeType);
        map.put("price", String.valueOf(totalFee / 100.0));
        map.put("order_id", orderNo);
        map.put("openid", openid);
//        map.put("order_uid", order_uid);
        map.put("notify_url", notifyUrl);
        map.put("sign", sign);
        String post = null;
        try {
            post = HttpUtils.postByOrigin("https://xorpay.com/api/pay/" + aid, map);
            logger.info(post);
            WxOrderRespVO respVO = new WxOrderRespVO();
            JSONObject jsonObject = JSONObject.parseObject(post);
            if ("ok".equalsIgnoreCase(jsonObject.getString("status"))) {
                String transactionNo = jsonObject.getString("aoid");
                jsonObject = jsonObject.getJSONObject("info");
                respVO.setOrderNo(orderNo);
                respVO.setNonceStr(jsonObject.getString("nonceStr"));
                respVO.setPaySign(jsonObject.getString("paySign"));
                respVO.setTimeStamp(jsonObject.getString("timeStamp"));
                respVO.setPayStr(jsonObject.getString("package"));
                respVO.setAppid(jsonObject.getString("appId"));
                // 更新第三方订单号
                if (!StringUtils.isEmpty(transactionNo)) {
                    UpdateWrapper wrapper = new UpdateWrapper();
                    wrapper.set("transation_no", transactionNo);
                    wrapper.eq("order_no", orderNo);
                    orderService.update(wrapper);
                }
                return respVO;
            } else {
                throw new BusinessException(500, jsonObject.getString("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("下单失败，参数：{}，错误信息：{}", JSONObject.toJSONString(map), e.getLocalizedMessage());
            throw new BusinessException(500, e.getLocalizedMessage());
        }
    }

    @Override
    public String payResultNotify(Map<String, String> params) {
        String orderNo = params.get("orderNo");
        synchronized (this) {
            Order order = orderService.getById(orderNo);
            if (order == null
                    || order.getStatus() == SysConstants.OrderStatus.PAYED
                    || order.getStatus() == SysConstants.OrderStatus.REFUNDED)
                return PayService.SUCCESS;
            String payStatus = params.get("status");
            if ("success".equalsIgnoreCase(payStatus) || "payed".equalsIgnoreCase(payStatus)) {
                logger.info("订单号：{} 已支付", orderNo);
                UpdateWrapper wrapper = new UpdateWrapper();
                wrapper.eq("order_no", orderNo);
                wrapper.set("status", SysConstants.OrderStatus.PAYED);
                wrapper.set("update_time", new Date());
                if (orderService.update(wrapper)) {
                    // 短信通知客户购买成功
                    smsService.sendBuyMsg(orderNo);
                    // 更新库存
                    prodPropertyLinkService.minusRemainByOrder(orderNo, order.getProdId());
                    // 更新销量
                    productMapper.addSellCount(order.getProdId(), order.getProdCount());
                    return SUCCESS;
                }
            }
            logger.warn("支付未完成：{}", params);
        }
        return FAIL;
    }

    @Override
    public Map<String, String> orderQuery(String outTradeNo) {
        try {
            String res = HttpUtils.getByOrigin("https://xorpay.com/api/query2/" + aid +
                    "?order_id=" + outTradeNo +
                    "&sign=" + MD5Utils.generate(outTradeNo + appsecret));
            JSONObject jsonObject = JSONObject.parseObject(res);
            Map<String, String> map = new HashMap<>();
            for (String key : jsonObject.keySet()) {
                map.put(key, jsonObject.getString(key));
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(500, e.getLocalizedMessage());
        }
    }

    @Override
    public Map<String, String> refundQuery(String outTradeNo) {
        return null;
    }

    @Override
    public Map<String, String> refund(String orderNo, String refundNo, int totalFee, int refundFee, String reason) {
        if (StringUtils.isEmpty(orderNo))
            throw new BusinessException(ResponseStatus.ORDER_INVALID);
        double price = totalFee/100.0;
        String params = "price=" + price;
        params += "&sign=" + MD5Utils.Bit32(price + this.appsecret);
        String result = HttpUtils.post("https://xorpay.com/api/refund/" + orderNo, params);
        logger.info("Refund result:{}", result);
        JSONObject resultJson = JSONObject.parseObject(result);
        logger.info("Order refund, order no:{}, total fee:{}, reason:{}", orderNo, totalFee, reason);
        Map<String, String> res = new HashMap<>();
        if ("ok".equalsIgnoreCase(resultJson.getString("status"))){ // 退款成功
            res.put("result_code", "SUCCESS");
            logger.info("Order no:{} refund success.", orderNo);
        } else { // 退款失败
            res.put("result_code", resultJson.getString("status"));
            logger.warn("Order no:{} refund failed.", orderNo);
            res.put("err_code_des", resultJson.getString("info"));
            if (StringUtils.isEmpty(res.get("err_code_des")))
                res.put("err_code_des", resultJson.getString("status"));
        }
        return res;
    }

    @Override
    public Map<String, String> closeOrder(String outTradeNo) {
        return null;
    }

    @Override
    public Map<String, String> downloadbill(String date) {
        return null;
    }
}
