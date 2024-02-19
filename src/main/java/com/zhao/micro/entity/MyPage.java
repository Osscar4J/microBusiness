package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 自定义页面
 */
@TableName("page_tb")
public class MyPage extends UpdateEntity {

    private static final long serialVersionUID = -5356931542165424762L;
    private Integer type;
    private String name;
    private String mark;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
