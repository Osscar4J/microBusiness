package com.zhao.micro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("user_tb")
public class User extends UpdateEntity {

    protected String account;
    protected String passwd;
    protected String phone;
    @TableField("role_id")
    protected Long roleId;
    @TableField("recommend_id")
    protected Long recommendId;
    protected Integer level;
    protected String email;
    protected Integer gender;
    protected String nickname;
    /**
     * 下线总数
     */
    @TableField("proxy_count")
    protected Integer proxyCount;
    @TableField("login_days")
    protected Integer loginDays;
    @TableField("max_login_days")
    protected Integer maxLoginDays;
    @TableField("last_login_time")
    protected Date lastLoginTime;
    protected Date birthday;
    protected String avatar;

    @TableField(exist = false)
    private Address address;
    @TableField(exist = false)
    private Role role;
    /**
     * 今日销量
     */
    @TableField(exist = false)
    private Long tc;
    /**
     * 昨天销量
     */
    @TableField(exist = false)
    private Long yc;
    /**
     * 上周销量
     */
    @TableField(exist = false)
    private Long lwc;
    /**
     * 本月销量
     */
    @TableField(exist = false)
    private Long tmc;
    /**
     * 销售统计总额
     */
    @TableField(exist = false)
    private Long amount;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Long recommendId) {
        this.recommendId = recommendId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getProxyCount() {
        return proxyCount;
    }

    public void setProxyCount(Integer proxyCount) {
        this.proxyCount = proxyCount;
    }

    public Integer getLoginDays() {
        return loginDays;
    }

    public void setLoginDays(Integer loginDays) {
        this.loginDays = loginDays;
    }

    public Integer getMaxLoginDays() {
        return maxLoginDays;
    }

    public void setMaxLoginDays(Integer maxLoginDays) {
        this.maxLoginDays = maxLoginDays;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getTmc() {
        return tmc;
    }

    public void setTmc(Long tmc) {
        this.tmc = tmc;
    }

    public Long getLwc() {
        return lwc;
    }

    public void setLwc(Long lwc) {
        this.lwc = lwc;
    }

    public Long getYc() {
        return yc;
    }

    public void setYc(Long yc) {
        this.yc = yc;
    }

    public Long getTc() {
        return tc;
    }

    public void setTc(Long tc) {
        this.tc = tc;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
