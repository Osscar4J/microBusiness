package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 域名
 */
@TableName("domain_tb")
public class Domain extends UpdateEntity {

    private String name;
    private String ip;
    private String provider;
    private String mark;
    private Integer type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
