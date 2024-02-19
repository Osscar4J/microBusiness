package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 用户名片二维码
 */
@TableName("user_qrcode_tb")
public class UserQrcode extends BaseEntity {

    @TableField("user_id")
    private Long userId;
    private Integer status;
    private String title;
    @TableField("file_url")
    private String fileUrl;
    private String content;
    /**
     * 1：随机显示，0：不随机，按阈值切换
     */
    @TableField("is_random")
    private Integer isRandom;
    @TableField("change_limit")
    private Integer changeLimit;

    /**
     * 微信二维码数量
     */
    @TableField(exist = false)
    private Integer wxCount;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsRandom() {
        return isRandom;
    }

    public void setIsRandom(Integer isRandom) {
        this.isRandom = isRandom;
    }

    public Integer getChangeLimit() {
        return changeLimit;
    }

    public void setChangeLimit(Integer changeLimit) {
        this.changeLimit = changeLimit;
    }

    public Integer getWxCount() {
        return wxCount;
    }

    public void setWxCount(Integer wxCount) {
        this.wxCount = wxCount;
    }
}
