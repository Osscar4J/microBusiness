package com.zhao.micro.model;

import java.io.Serializable;

public class AmountModel implements Serializable {
    private String date;
    private Long sum;

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
