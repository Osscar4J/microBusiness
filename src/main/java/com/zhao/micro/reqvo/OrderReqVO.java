package com.zhao.micro.reqvo;

public class OrderReqVO extends BaseReqVO {

    private String name;
    private String orderNo;
    private Long sellerId;
    /**
     * 0：未发货，1：已发货
     */
    private Integer emsStatus;
    private String phone;
    /**
     * 快递单号
     */
    private String emsNo;
    private String month;
    private String st;
    private String et;
    private String orderNoOrPhone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getEmsStatus() {
        return emsStatus;
    }

    public void setEmsStatus(Integer emsStatus) {
        this.emsStatus = emsStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmsNo() {
        return emsNo;
    }

    public void setEmsNo(String emsNo) {
        this.emsNo = emsNo;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getEt() {
        return et;
    }

    public void setEt(String et) {
        this.et = et;
    }

    public String getOrderNoOrPhone() {
        return orderNoOrPhone;
    }

    public void setOrderNoOrPhone(String orderNoOrPhone) {
        this.orderNoOrPhone = orderNoOrPhone;
    }
}
