package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品-商品属相 关联表
 */
@TableName("prod_property_lk_tb")
public class ProdPropertyLink implements Serializable {

    @TableField("prod_id")
    private Long prodId;
    @TableField("property_id")
    private Long propertyId;
    /**
     * 属性选项id
     */
    @TableField("property_item_id")
    private Long propertyItemId;
    @TableField("create_by")
    private Long createBy;
    @TableField("create_time")
    private Date createTime;
    /**
     * 该类型所剩库存
     */
    private Integer remain;
    private String name;

    @TableField(exist = false)
    private String propertyName;

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public Long getPropertyItemId() {
        return propertyItemId;
    }

    public void setPropertyItemId(Long propertyItemId) {
        this.propertyItemId = propertyItemId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
