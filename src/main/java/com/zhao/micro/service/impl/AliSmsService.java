package com.zhao.micro.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.zhao.micro.api.EmsApi;
import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.entity.Ems;
import com.zhao.micro.entity.Order;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.model.MessageModel;
import com.zhao.micro.oss.AliOSSConfig;
import com.zhao.micro.reqvo.OrderReqVO;
import com.zhao.micro.service.EmsService;
import com.zhao.micro.service.OrderService;
import com.zhao.micro.service.SmsService;
import com.zhao.micro.sms.AliSMSConfig;
import com.zhao.micro.utils.HttpClient;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service
public class AliSmsService implements SmsService {

    private Logger logger = Logger.getLogger(getClass());

    final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
    final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
    private IClientProfile profile = null;

    @Autowired
    private OrderService orderService;
    @Autowired
    private EmsService emsService;
    @Autowired
    private EmsApi emsApi;
    @Value("${sms-host}")
    private String smsHost;
    @Value("${sms-token}")
    private String smsToken;

    static {
        // 设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
    }

    @Transactional
    @Override
    public void sendEmsMsg(String orderNo) {
        Order order = getOrderDetailByOrderNo(orderNo);
        String phone = order.getUser().getPhone();
        JSONObject params = new JSONObject();
        params.put("orderNo", orderNo);
        params.put("emsNo", order.getEms().getEmsNo());
        params.put("emsName", order.getEms().getName());
        sendMsg(AliSMSConfig.TEMPLATE_EMS, phone, JSONObject.toJSONString(params));
        Ems ems = emsApi.getByOrderNo(orderNo).getContent();
        ems.setSmsTimes(ems.getSmsTimes() + 1);
        emsService.updateById(ems);
    }

    @Async
    @Override
    public void sendBuyMsg(String orderNo) {
        Order order = getOrderDetailByOrderNo(orderNo);
        String phone = order.getUser().getPhone();
        JSONObject params = new JSONObject();
        params.put("prodName", order.getName());
        params.put("orderNo", orderNo);
        sendMsg(AliSMSConfig.TEMPLATE_BUY, phone, JSONObject.toJSONString(params));
    }

    private Order getOrderDetailByOrderNo(String orderNo) {
        if (StringUtils.isEmpty(orderNo))
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        OrderReqVO reqVO = new OrderReqVO();
        reqVO.setOrderNo(orderNo);
        return orderService.getDetail(reqVO);
    }

    @Override
    public ResponseStatus sendMsg(String template, String phone, String params) {
//        if (profile == null) {
//            profile = DefaultProfile.getProfile("cn-shenzhen", AliOSSConfig.ACCESS_KEY, AliOSSConfig.ACCESS_SECRET);
//            try {
//                DefaultProfile.addEndpoint("cn-shenzhen", "cn-shenzhen", product, domain);
//            } catch (ClientException e) {
//                logger.error(e);
//                return ResponseStatus.FAILURE;
//            }
//        }
//        IAcsClient acsClient = new DefaultAcsClient(profile);
//        SendSmsRequest request = new SendSmsRequest();
//        request.setMethod(MethodType.POST);
//        request.setPhoneNumbers(phone);
//        request.setSignName(AliSMSConfig.SIGN_NAME);
//        request.setTemplateCode(template);
//        request.setTemplateParam(params);
//        try {
//            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
//            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().toLowerCase().equals("ok")) {
//                return ResponseStatus.SUCCESS;
//            } else {
//                if (logger.isEnabledFor(Level.WARN))
//                    logger.warn(phone + "短信发送失败：" + sendSmsResponse.getMessage());
//            }
//        } catch (Exception e) {
//            throw new BusinessException(500, e.getLocalizedMessage());
//        }
        MessageModel messageModel = new MessageModel();
        messageModel.setPhone(phone);
        messageModel.setToken(smsToken);
        messageModel.setSign(AliSMSConfig.SIGN_NAME);
        messageModel.setTemplate(template);
        messageModel.setPhone(phone);
        messageModel.setParams(params);
        String res = HttpClient.doPost(smsHost, JSONObject.toJSONString(messageModel));
        if ("200".equalsIgnoreCase(res))
            return ResponseStatus.SUCCESS;
        logger.error("短信发送失败: " + res);
        return ResponseStatus.FAILURE;
    }
}
