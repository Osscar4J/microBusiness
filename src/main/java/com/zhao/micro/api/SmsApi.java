package com.zhao.micro.api;

import com.alibaba.fastjson.JSONObject;
import com.zhao.micro.annotation.CommonPath;
import com.zhao.micro.annotation.HasRole;
import com.zhao.micro.annotation.LoginRequired;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.respvo.BaseResponse;
import com.zhao.micro.service.SmsService;
import com.zhao.micro.sms.AliSMSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@LoginRequired
@RestController
@RequestMapping("/api/sms")
public class SmsApi {

    @Lazy
    @Autowired
    private SmsService smsService;

    /**
     * 发货通知短信
     *
     * @param orderNo 订单号
     * @return
     */
    @HasRole(SysConstants.Role.ADMIN)
    @GetMapping("/ems/{orderNo}")
    public BaseResponse sendEmsMsg(@PathVariable String orderNo) {
        smsService.sendEmsMsg(orderNo);
        return BaseResponse.SUCCESS();
    }

    @CommonPath
    @GetMapping("/test")
    public BaseResponse test(String phone) {
        JSONObject params = new JSONObject();
        params.put("prodName", "【炒米粉】");
        params.put("orderNo", "99999999");
        return BaseResponse.SUCCESS(smsService.sendMsg(AliSMSConfig.TEMPLATE_BUY, phone, JSONObject.toJSONString(params)));
    }

}
