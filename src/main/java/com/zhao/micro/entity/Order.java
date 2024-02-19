package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单
 */
@TableName("order_tb")
public class Order implements Serializable {

    @TableId(type = IdType.INPUT)
    @TableField("order_no")
    private String orderNo;
    @TableField("transation_no")
    private String transationNo;
    private Integer status;
    private String name;
    @TableField("prod_id")
    private Long prodId;
    @TableField("user_id")
    private Long userId;
    @TableField("seller_id")
    private Long sellerId;
    @TableField("proxy_id")
    private Long proxyId;
    @TableField("total_fee")
    private Integer totalFee;
    @TableField("prod_count")
    private Integer prodCount;
    /**
     * 快递id，如果没有发货则为-1（默认）
     */
    @TableField("ems_id")
    private Long emsId;
    @TableField("pay_style")
    private Integer payStyle;
    @TableField("create_time")
    private Date createTime;
    @TableField("pay_time")
    private Date payTime;
    @TableField("update_time")
    private Date updateTime;
    @TableField("prod_property")
    private String prodProperty;
    private String mark;
    /**
     * 退款单号
     */
    @TableField("refund_no")
    private String refundNo;
    /**
     * 退款时间
     */
    @TableField("refund_time")
    private Date refundTime;
    // 退款原因
    private String reason;

    @TableField(exist = false)
    private Product product;
    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private Ems ems;
    @TableField(exist = false)
    private User seller;
    @TableField(exist = false)
    private User proxy;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTransationNo() {
        return transationNo;
    }

    public void setTransationNo(String transationNo) {
        this.transationNo = transationNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getProdCount() {
        return prodCount;
    }

    public void setProdCount(Integer prodCount) {
        this.prodCount = prodCount;
    }

    public Integer getPayStyle() {
        return payStyle;
    }

    public void setPayStyle(Integer payStyle) {
        this.payStyle = payStyle;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getProdProperty() {
        return prodProperty;
    }

    public void setProdProperty(String prodProperty) {
        this.prodProperty = prodProperty;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getEmsId() {
        return emsId;
    }

    public void setEmsId(Long emsId) {
        this.emsId = emsId;
    }

    public Ems getEms() {
        return ems;
    }

    public void setEms(Ems ems) {
        this.ems = ems;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public User getProxy() {
        return proxy;
    }

    public void setProxy(User proxy) {
        this.proxy = proxy;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }
}
