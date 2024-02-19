package com.zhao.micro.service;

import com.zhao.micro.constants.ResponseStatus;

public interface SmsService {

    /**
     * 发货通知短信
     *
     * @param orderNo 订单号
     */
    void sendEmsMsg(String orderNo);

    /**
     * 购买成功通知短信
     *
     * @param orderNo 订单号
     */
    void sendBuyMsg(String orderNo);

    ResponseStatus sendMsg(String template, String phone, String params);
}
