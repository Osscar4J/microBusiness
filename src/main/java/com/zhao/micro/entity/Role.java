package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 角色
 */
@TableName("role_tb")
public class Role extends UpdateEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
