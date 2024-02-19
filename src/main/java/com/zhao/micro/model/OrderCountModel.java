package com.zhao.micro.model;

public class OrderCountModel {
    private Integer total;
    private Integer ems;
    private Integer nems;

    public Integer getNems() {
        return nems;
    }

    public void setNems(Integer nems) {
        this.nems = nems;
    }

    public Integer getEms() {
        return ems;
    }

    public void setEms(Integer ems) {
        this.ems = ems;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
