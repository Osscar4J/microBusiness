package com.zhao.micro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.WxMenu;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.mapper.WxMenuMapper;
import com.zhao.micro.service.MyBaseService;
import com.zhao.micro.service.WxMenuService;
import com.zhao.micro.utils.WeChatUtils;
import com.zhao.micro.wx.model.menu.CommonButton;
import com.zhao.micro.wx.model.menu.ComplexButton;
import com.zhao.micro.wx.model.menu.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WxMenuServiceImpl extends MyBaseService<WxMenuMapper, WxMenu> implements WxMenuService {

    @Autowired
    private WxMenuMapper wxMenuMapper;

    @Override
    public boolean createWechatMenus(List<WxMenu> menus) {
        if (menus == null)
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);

        List<ComplexButton> mainButtons = new ArrayList<>();

        Date now = new Date();
        menus.stream()
                .forEach(m -> {
                    ComplexButton button = new ComplexButton();
                    button.setName(m.getName());
                    m.setCreateTime(now);

                    List<CommonButton> subBtns = new ArrayList<>();
                    m.getSubList()
                            .stream()
                            .forEach(sm -> {
                                sm.setCreateTime(now);
                                CommonButton btn = new CommonButton();
                                btn.setKey(sm.getWxKey());
                                btn.setType(sm.getType());
                                btn.setName(sm.getName());
                                if (!StringUtils.isEmpty(sm.getUrl()))
                                    btn.setUrl(sm.getUrl());
                                if (!StringUtils.isEmpty(sm.getAppid()))
                                    btn.setAppid(sm.getAppid());
                                if (!StringUtils.isEmpty(sm.getPagepath()))
                                    btn.setPagepath(sm.getPagepath());
                                subBtns.add(btn);
                            });
                    button.setSub_button(subBtns);
                    if (!button.getSub_button().isEmpty())
                        mainButtons.add(button);
                });

        Menu menu = new Menu();
        menu.setButton(mainButtons);
        WeChatUtils.createMenus(menu, WeChatUtils.getAccessToken(SysConstants.GH_APPID, SysConstants.GH_APP_SECRET));
        return true;
    }

    @Override
    public boolean saveOrUpdate(WxMenu entity) {
        if (entity.getId() == null)
            return this.save(entity);
        return this.updateById(entity);
    }

    @Override
    public void sync2Wechat() {
        this.createWechatMenus(wxMenuMapper.selectAllByStatus(1));
    }

    @Override
    public List<WxMenu> getAll() {
        return wxMenuMapper.selectAllByStatus(null);
    }

    @Override
    public WxMenu getByKey(String key) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("wx_key", key);
        return this.getOne(wrapper);
    }

}
