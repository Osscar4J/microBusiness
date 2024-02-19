package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

public class UpdateEntity extends BaseEntity {

    @TableField("update_time")
    protected Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
