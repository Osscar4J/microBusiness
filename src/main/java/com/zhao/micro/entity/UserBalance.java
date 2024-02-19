package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户余额信息（积分、余额等）
 */
@TableName("user_balance_tb")
public class UserBalance implements Serializable {

    @TableId(type = IdType.INPUT)
    @TableField("user_id")
    private Long userId;
    private Integer status;
    private Integer credit;
    @TableField("freeze_credit")
    private Integer freezeCredit;
    private Integer balance;
    @TableField("freeze_balance")
    private Integer freezeBalance;
    @TableField("create_time")
    private Date createTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getFreezeCredit() {
        return freezeCredit;
    }

    public void setFreezeCredit(Integer freezeCredit) {
        this.freezeCredit = freezeCredit;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getFreezeBalance() {
        return freezeBalance;
    }

    public void setFreezeBalance(Integer freezeBalance) {
        this.freezeBalance = freezeBalance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
