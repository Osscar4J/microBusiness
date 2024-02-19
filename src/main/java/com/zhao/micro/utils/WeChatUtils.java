package com.zhao.micro.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.wx.model.AccessToken;
import com.zhao.micro.wx.model.menu.Menu;
import org.apache.log4j.Logger;

import java.io.File;

public class WeChatUtils {
    private static AccessToken accessToken = null;
    private static Logger logger = Logger.getLogger(WeChatUtils.class);

    private WeChatUtils() {
    }

    public static String addTempMedia(String appId, String appSecret, String filePath) throws Exception {
        return addTempMedia(appId, appSecret, new File(filePath));
    }

    public static String addTempMedia(String appId, String appSecret, File file) throws Exception {
        String res = HttpUtils.upload("https://api.weixin.qq.com/cgi-bin/media/upload" +
                "?access_token=" + getAccessToken(appId, appSecret) +
                "&type=image", file, "media.jpg");
        return JSONObject.parseObject(res).getString("media_id");
    }

    // {"ticket":"gQEy8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyYVFzcnN6WGJlWjMxMDAwME0wMzkAAgS7EcRdAwQAAAAA","url":"http:\/\/weixin.qq.com\/q\/02aQsrszXbeZ310000M039"}
    public static String getTicket(String appId, String appSecret, long userId) {
        JSONObject params = new JSONObject();
        params.put("action_name", "QR_SCENE");
        params.put("expire_seconds", 2592000); // 30天

        JSONObject actionInfo = new JSONObject();
        JSONObject scene = new JSONObject();
        scene.put("scene_id", userId);
        actionInfo.put("scene", scene);

        params.put("action_info", actionInfo);

        String res = HttpUtils.post("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + WeChatUtils.getAccessToken(appId, appSecret), params.toJSONString());
        return JSONObject.parseObject(res).getString("ticket");
    }

    public static JSONObject getUserInfo(String accessToken, String openid) {
        String res = HttpUtils.get("https://api.weixin.qq.com/cgi-bin/user/info" +
                "?access_token=" + accessToken +
                "&openid=" + openid +
                "&lang=zh_CN");
        return JSONObject.parseObject(res);
    }

    public static String getAccessToken(String appId, String appSecret) {
        if (accessToken == null
                || System.currentTimeMillis() - accessToken.getStartTime() > 7000000
                || accessToken.getAccessToken() == null) { // 两小时内有效
            String res = HttpUtils.get("https://api.weixin.qq.com/cgi-bin/token" +
                    "?grant_type=client_credential" +
                    "&appid=" + appId +
                    "&secret=" + appSecret);
            JSONObject resJson = JSONObject.parseObject(res);

            if (accessToken == null)
                accessToken = new AccessToken();

            accessToken.setAccessToken(resJson.getString("access_token"));
            accessToken.setStartTime(System.currentTimeMillis());
            if (logger.isInfoEnabled())
                logger.info("获取accessToken: " + res);
        }
        return accessToken.getAccessToken();
    }

    /**
     * 创建菜单
     *
     * @param menu
     * @param accessToken
     */
    public static void createMenus(Menu menu, String accessToken) {
        String res = HttpUtils.post("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken,
                JSONObject.toJSONString(menu));
        JSONObject resJson = JSONObject.parseObject(res);
        if (resJson != null) {
            if (resJson.getInteger("errcode") != 0) {
                throw new BusinessException(500, "菜单创建失败！code:" + resJson.getInteger("errcode"));
            }
        }
    }

    /**
     * 查询菜单信息
     *
     * @param accessToken
     * @return
     */
    public static String getMenus(String accessToken) {
        return HttpUtils.get("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + accessToken);
    }

}
