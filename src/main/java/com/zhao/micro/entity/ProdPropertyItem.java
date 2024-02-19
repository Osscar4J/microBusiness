package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 商品参数选项
 */
@TableName("prod_property_item_tb")
public class ProdPropertyItem extends BaseEntity {

    @TableField("property_id")
    private Long propertyId;
    private String name;
    @TableField("creatae_by")
    private Long creataeBy;

    @TableField(exist = false)
    private Integer remain;
    @TableField(exist = false)
    private ProdPropertyLink link;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreataeBy() {
        return creataeBy;
    }

    public void setCreataeBy(Long creataeBy) {
        this.creataeBy = creataeBy;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public ProdPropertyLink getLink() {
        return link;
    }

    public void setLink(ProdPropertyLink link) {
        this.link = link;
    }
}
