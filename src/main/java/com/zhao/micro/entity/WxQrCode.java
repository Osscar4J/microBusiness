package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 微信名片二维码
 */
@TableName("wx_qrcode_tb")
public class WxQrCode extends UpdateEntity {

    private static final long serialVersionUID = -3731530556682261782L;
    @TableField("user_id")
    private Long userId;
    /**
     * 活码id
     */
    @TableField("qrcode_id")
    private Long qrcodeId;
    /**
     * 扫码次数
     */
    @TableField("scan_times")
    private Integer scanTimes;
    /**
     * 长按添加好友次数
     */
    @TableField("add_times")
    private Integer addTimes;
    private String title;
    private String description;
    @TableField("file_url")
    private String fileUrl;
    @TableField("change_limit")
    private Integer changeLimit;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(Long qrcodeId) {
        this.qrcodeId = qrcodeId;
    }

    public Integer getScanTimes() {
        return scanTimes;
    }

    public void setScanTimes(Integer scanTimes) {
        this.scanTimes = scanTimes;
    }

    public Integer getAddTimes() {
        return addTimes;
    }

    public void setAddTimes(Integer addTimes) {
        this.addTimes = addTimes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getChangeLimit() {
        return changeLimit;
    }

    public void setChangeLimit(Integer changeLimit) {
        this.changeLimit = changeLimit;
    }
}
