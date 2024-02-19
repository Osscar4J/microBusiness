package com.zhao.micro.model;

import com.zhao.micro.entity.ProdPropertyItem;

import java.util.List;

public class OrderModel {
    private Long prodId; // 产品id
    private List<ProdPropertyItem> properties; // 产品参数
    private int count = 1; // 购买数量
    private String receiver; // 收件人姓名
    private String phone; // 手机号
    private Long pId; // 省id
    private Long cId; // 市id
    private Long sId; // 区id
    private String addr; // 详细地址
    private String mark; // 备注
    private String cacheCode; // 产品链接在缓存中的key
    private String userIp;

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public List<ProdPropertyItem> getProperties() {
        return properties;
    }

    public void setProperties(List<ProdPropertyItem> properties) {
        this.properties = properties;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public Long getcId() {
        return cId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    public Long getsId() {
        return sId;
    }

    public void setsId(Long sId) {
        this.sId = sId;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCacheCode() {
        return cacheCode;
    }

    public void setCacheCode(String cacheCode) {
        this.cacheCode = cacheCode;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }
}
