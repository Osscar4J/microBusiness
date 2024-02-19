package com.zhao.micro.api;

import com.zhao.micro.annotation.CommonPath;
import com.zhao.micro.annotation.HasRole;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.WxMenu;
import com.zhao.micro.respvo.BaseResponse;
import com.zhao.micro.service.WxMenuService;
import com.zhao.micro.utils.WechatCheckUtil;
import com.zhao.micro.wx.model.WechatMessageUtil;
import com.zhao.micro.wx.model.WechatTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@HasRole(SysConstants.Role.ADMIN)
@RestController
@RequestMapping("/api/wechat")
public class WechatApi {

    @Autowired
    private WxMenuService wxMenuService;

    @Value("${auth-redirect-domain}")
    private String authDomain;

    @GetMapping("/menus")
    public BaseResponse getMenus() {
        return BaseResponse.SUCCESS(wxMenuService.getAll());
    }

    @HasRole(SysConstants.Role.ADMIN)
    @PostMapping("/menu")
    public BaseResponse createMenus(@RequestBody WxMenu entity) {
        return BaseResponse.SUCCESS(wxMenuService.saveOrUpdate(entity));
    }

    /**
     * 菜单同步到微信公众号
     *
     * @return
     */
    @HasRole(SysConstants.Role.ADMIN)
    @PostMapping("/menu/sync")
    public BaseResponse sync2Wechat() {
        wxMenuService.sync2Wechat();
        return BaseResponse.SUCCESS();
    }

    @HasRole(SysConstants.Role.ADMIN)
    @DeleteMapping("/menu/{id}")
    public BaseResponse removeMenu(@PathVariable long id) {
        return BaseResponse.SUCCESS(wxMenuService.removeById(id));
    }

    @CommonPath
    @RequestMapping(value = "/check")
    public void wechatService(HttpServletResponse response, HttpServletRequest request,
                              @RequestParam(value = "signature", required = false) String signature, String timestamp,
                              String nonce, String echostr) throws IOException {

        if (echostr == null) {
            if (WechatCheckUtil.checkSignature(signature, timestamp, nonce, SysConstants.GH_TOKEN)) {
                PrintWriter out = response.getWriter();
                final Map<String, String> map = WechatMessageUtil.xmlToMap(request);
                // 发送方帐号（一个OpenID）
                final String fromUserName = map.get("FromUserName");
                // 开发者微信号
                final String ghId = map.get("ToUserName");
                // 消息类型
                String msgType = map.get("MsgType");
                String temp = map.get("Content");
                if (temp == null)
                    temp = "";
                temp = temp.trim();

                String[] imeiList = temp.split("\n");
                // 默认回复一个"success"
                String responseMessage = "success";

                WechatTextMessage textMessage = new WechatTextMessage();
                textMessage.setMsgType(WechatMessageUtil.MESSAGE_TEXT);
                textMessage.setToUserName(fromUserName);
                textMessage.setFromUserName(ghId);
                textMessage.setCreateTime(System.currentTimeMillis());
                textMessage.setContent(responseMessage);

                if (!StringUtils.isEmpty(textMessage.getContent())) {
                    responseMessage = WechatMessageUtil.textMessageToXml(textMessage);
                    responseMessage = new String(responseMessage.getBytes("UTF-8"), "iso-8859-1");
                    out.print(responseMessage);
                    out.flush();
                }
            }
        } else { // 验证
            PrintWriter out = response.getWriter();
            out.print(echostr);
            out.flush();
        }
    }
}
