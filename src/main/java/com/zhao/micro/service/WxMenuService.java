package com.zhao.micro.service;

import com.zhao.micro.entity.WxMenu;

import java.util.List;

public interface WxMenuService extends BaseService<WxMenu> {

    boolean createWechatMenus(List<WxMenu> menus);

    void sync2Wechat();

    List<WxMenu> getAll();

    WxMenu getByKey(String key);

}
