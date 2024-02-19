package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 微信菜单
 */
@TableName("wx_menu_tb")
public class WxMenu implements Serializable {

    private static final long serialVersionUID = -1476127783887657108L;
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("parent_id")
    private Long parentId;
    private Integer status;
    /**
     * 菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型
     */
    private String type;
    @TableField("wx_key")
    private String wxKey;
    /**
     * 1: 1级菜单， 2： 2级菜单
     */
    @TableField("menu_lv")
    private Integer menuLv;
    @TableField("sort_no")
    private Integer sortNo;
    @TableField("service_id")
    private Long serviceId;
    private String name;
    /**
     * view、miniprogram类型必须
     * 网页链接，用户点击菜单可打开链接，不超过1024字节。当type为miniprogram时，不支持小程序的老版本客户端将打开本url
     */
    private String url;
    /**
     * miniprogram类型必须
     * 小程序的appid
     */
    private String appid;
    /**
     * miniprogram类型必须
     * 小程序的页面路径
     */
    private String pagepath;
    @TableField("create_time")
    private Date createTime;
    private String info;

    @TableField(exist = false)
    private List<WxMenu> subList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getWxKey() {
        return wxKey;
    }

    public void setWxKey(String wxKey) {
        this.wxKey = wxKey;
    }

    public Integer getMenuLv() {
        return menuLv;
    }

    public void setMenuLv(Integer menuLv) {
        this.menuLv = menuLv;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<WxMenu> getSubList() {
        return subList;
    }

    public void setSubList(List<WxMenu> subList) {
        this.subList = subList;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }
}
