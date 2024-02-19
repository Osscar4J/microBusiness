package com.zhao.micro.reqvo;

public class BaseReqVO {
    private int current = 1;
    private int size = 10;
    private int needPage = 1;
    private Integer status;
    private Long id;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNeedPage() {
        return needPage;
    }

    public void setNeedPage(int needPage) {
        this.needPage = needPage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
