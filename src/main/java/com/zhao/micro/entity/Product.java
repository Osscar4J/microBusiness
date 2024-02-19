package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

/**
 * 商品
 */
@TableName("product_tb")
public class Product extends UpdateEntity {

    private String name;
    /**
     * 点击量
     */
    @TableField("click_count")
    private Long clickCount;
    /**
     * 价格
     */
    private Integer price;
    @TableField(exist = false)
    private Double priceF;
    /**
     * 原价
     */
    @TableField("origin_price")
    private Integer originPrice;
    @TableField(exist = false)
    private Double originPriceF;
    /**
     * 库存
     */
    private Integer remain;
    /**
     * 销量
     */
    @TableField("sell_count")
    private Integer sellCount;
    /**
     * 实际销量
     */
    @TableField("act_sell_count")
    private Integer actSellCount;
    @TableField("create_by")
    private Long createBy;
    @TableField("update_by")
    private Long updateBy;
    private String summary;
    private String cover;
    private String content;

    /**
     * 商品属性
     */
    @TableField(exist = false)
    private List<ProdProperty> properties;
    /**
     * banner图列表
     */
    @TableField(exist = false)
    private List<ProdPic> banners;
    /**
     * 产品和产品属性的关联信息,含库存
     */
    @TableField(exist = false)
    private List<ProdPropertyLink> remains;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(Integer originPrice) {
        this.originPrice = originPrice;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public Integer getSellCount() {
        return sellCount;
    }

    public void setSellCount(Integer sellCount) {
        this.sellCount = sellCount;
    }

    public Integer getActSellCount() {
        return actSellCount;
    }

    public void setActSellCount(Integer actSellCount) {
        this.actSellCount = actSellCount;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ProdProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ProdProperty> properties) {
        this.properties = properties;
    }

    public List<ProdPic> getBanners() {
        return banners;
    }

    public void setBanners(List<ProdPic> banners) {
        this.banners = banners;
    }

    public Double getPriceF() {
        if (this.price == null)
            return 0D;
        return price / 100D;
    }

    public Double getOriginPriceF() {
        if (this.originPrice == null)
            return 0D;
        return this.originPrice / 100D;
    }

    public List<ProdPropertyLink> getRemains() {
        return remains;
    }

    public void setRemains(List<ProdPropertyLink> remains) {
        this.remains = remains;
    }
}
