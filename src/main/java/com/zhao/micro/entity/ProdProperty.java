package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

/**
 * 商品属性
 */
@TableName("pproperty_tb")
public class ProdProperty extends UpdateEntity {

    private String name;
    @TableField("create_by")
    private Long createBy;

    @TableField(exist = false)
    private List<ProdPropertyItem> items;
    /**
     * 库存
     */
    @TableField(exist = false)
    private Integer remain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public List<ProdPropertyItem> getItems() {
        return items;
    }

    public void setItems(List<ProdPropertyItem> items) {
        this.items = items;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }
}
