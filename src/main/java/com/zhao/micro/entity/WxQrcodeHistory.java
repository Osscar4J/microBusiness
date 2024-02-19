package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信二维码识别记录
 */
@TableName("wxqrcode_history_tb")
public class WxQrcodeHistory implements Serializable {

    private static final long serialVersionUID = 188632924570189008L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private String ip;
    @TableField("qrcode_id")
    private Long qrcodeId;
    @TableField("wxqrcode_id")
    private Long wxqrcodeId;
    @TableField("create_time")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(Long qrcodeId) {
        this.qrcodeId = qrcodeId;
    }

    public Long getWxqrcodeId() {
        return wxqrcodeId;
    }

    public void setWxqrcodeId(Long wxqrcodeId) {
        this.wxqrcodeId = wxqrcodeId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
