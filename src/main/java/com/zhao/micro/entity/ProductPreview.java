package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品浏览日志
 */
@TableName("product_preview_tb")
public class ProductPreview implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("prod_id")
    private Long prodId;
    @TableField("create_time")
    private Date createTime;
    /**
     * 该商品所属的代理商id
     */
    @TableField("proxy_id")
    private Long proxyId;
    /**
     * 该商品所属的微商id
     */
    @TableField("seller_id")
    private Long sellerId;
    private String ip;
    private String province;
    private String city;

    @TableField(exist = false)
    private Product product;
    @TableField(exist = false)
    private User proxy;
    @TableField(exist = false)
    private User seller;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public User getProxy() {
        return proxy;
    }

    public void setProxy(User proxy) {
        this.proxy = proxy;
    }
}
