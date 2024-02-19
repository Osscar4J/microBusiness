package com.zhao.micro.pages;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.micro.annotation.HasRole;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.*;
import com.zhao.micro.model.ShareCacheModel;
import com.zhao.micro.reqvo.ProductReqVO;
import com.zhao.micro.reqvo.UserReqVO;
import com.zhao.micro.service.*;
import com.zhao.micro.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CommonPage {

    @Autowired
    private UserService userService;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserQrcodeService userQrcodeService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductPreviewService productPreviewService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private MyPageService myPageService;
    @Autowired
    private WxQrcodeHistoryService wxQrcodeHistoryService;
    @Autowired
    private WxQrCodeService wxQrCodeService;

    @Value("${kfphone}")
    private String kfPhone;

    @ResponseBody
    @RequestMapping({"", "/index"})
    public String blank() {
        return "";
    }

    @GetMapping("/manager/login")
    public String index(HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute(SysConstants.USER_IN_SESSION) != null)
            return "redirect:/user/index";
        Cookie cookie = getCookieByKey(request, SysConstants.Cookies.ACCOUNT);
        if (cookie != null) {
            model.addAttribute("account", cookie.getValue());
            cookie = getCookieByKey(request, SysConstants.Cookies.PASSWORD);
            if (cookie != null) {
                model.addAttribute("password", cookie.getValue());
                model.addAttribute("remember", true);
            }
        }
        return "login";
    }

    /**
     * 注册页面
     *
     * @param model
     * @param userId 推荐人id，注册成功后自动成为该推荐人的下线
     * @return
     */
    @GetMapping("/register/{userId}")
    public String register(Model model, @PathVariable Long userId) {
        model.addAttribute("userId", userId);
        return "register";
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("/register/{userId}")
    public String registerHandler(Model model, User user, @PathVariable Long userId) {
        try {
            user.setRoleId(SysConstants.Role.MICRO_BUSINESS);
            userService.register(user);
            return "redirect:/manager/login";
        } catch (Exception e) {
            model.addAttribute("userId", userId);
            model.addAttribute("errMsg", e.getLocalizedMessage());
            return "register";
        }
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model, UserReqVO reqVO) {
        if (StringUtils.isEmpty(reqVO.getAccount()) || StringUtils.isEmpty(reqVO.getPasswd())) {
            model.addAttribute("errMsg", "账号或密码不能为空");
            return "login";
        }
        try {
            User user = userService.login(reqVO);
            request.getSession().setAttribute(SysConstants.USER_IN_SESSION, user);
            // 记住账号、密码，cookie有效期30天
            if (reqVO.isRemember()) {
                addCookie(response, SysConstants.Cookies.ACCOUNT, reqVO.getAccount(), 2592000);
                addCookie(response, SysConstants.Cookies.PASSWORD, reqVO.getPasswd(), 2592000);
            } else { // 删除cookie
                removeCookie(request, response, SysConstants.Cookies.ACCOUNT);
                removeCookie(request, response, SysConstants.Cookies.PASSWORD);
            }
            // 记录登录日志
            reqVO.setIp(CommonUtils.getIpAddr(request));
            loginLogService.saveLog(reqVO, user);
            return "redirect:user/index";
        } catch (Exception e) {
            model.addAttribute("errMsg", e.getLocalizedMessage());
            return "login";
        }
    }

    /**
     * 各种转发码
     *
     * @param code 生成活码时随机生成的code
     * @return
     */
    @GetMapping("/{:\\w\\w}/{code}")
    public String userQrcode(@PathVariable String code, Model model, HttpServletRequest request) {
        ShareCacheModel cacheModel = (ShareCacheModel) cacheService.get(code);
        if (cacheModel == null)
            return "redirect:/";
        if (SysConstants.ShareCacheModelType.USER_ALIVE_QRCODE == cacheModel.getType()) { // 微信活码
            String url = userService.generateShareQrcode(SysConstants.ShareCacheModelType.USER_WX_QRCODE, cacheModel.getValue(), cacheModel.getUserId());
            cacheService.updateExpired(code, SysConstants.RedisTime.USER_ALIVE_CODE);
            return "redirect:" + url;
        } else if (SysConstants.ShareCacheModelType.USER_WX_QRCODE == cacheModel.getType()) { // 微信二维码页面
            // 微信二维码
            UserQrcode userQrcode = userQrcodeService.getById(cacheModel.getValue());
            QueryWrapper wrapper = new QueryWrapper();
            String ip = CommonUtils.getIpAddr(request);
            wrapper.eq("ip", ip);
            wrapper.eq("qrcode_id", userQrcode.getId());
            WxQrcodeHistory qrcodeHistory = wxQrcodeHistoryService.getOne(wrapper, false);
            WxQrCode wxQrCode = null;
            // 防追封，如果当前用户已经扫过这个码，则还返回他原来访问过的那个微信码
            if (qrcodeHistory != null) {
                wxQrCode = wxQrCodeService.getById(qrcodeHistory.getWxqrcodeId());
            }
            if (wxQrCode == null || qrcodeHistory == null) {
                if (userQrcode.getIsRandom() == 1) { // 随机获取一个微信码
                    wxQrCode = wxQrCodeService.getRandomByQrcodeId(userQrcode.getId());
                } else { // 顺序获取微信码，通过设置的阈值来切换
                    wxQrCode = wxQrCodeService.getByChangeLimit(userQrcode.getId());
                }
            }
            if (wxQrCode != null) {
                // 保存本次扫码记录
                qrcodeHistory = new WxQrcodeHistory();
                qrcodeHistory.setIp(ip);
                qrcodeHistory.setQrcodeId(userQrcode.getId());
                qrcodeHistory.setWxqrcodeId(wxQrCode.getId());
                wxQrcodeHistoryService.save(qrcodeHistory);

                // 增加扫码次数
                wxQrCodeService.updateScanTimes(wxQrCode.getId());

                if (StringUtils.isEmpty(wxQrCode.getTitle()))
                    wxQrCode.setTitle(userQrcode.getTitle());
                if (StringUtils.isEmpty(wxQrCode.getDescription()))
                    wxQrCode.setDescription(userQrcode.getContent());
            }
            model.addAttribute("qrcode", wxQrCode);
            cacheService.updateExpired(code, SysConstants.RedisTime.USER_ALIVE_CODE_URL);
            return "user/business/qrcode";
        } else if (SysConstants.ShareCacheModelType.PRODUCT_POPULARIZE == cacheModel.getType()) { // 产品推广
            String url = userService.generateShareQrcode(SysConstants.ShareCacheModelType.PRODUCT_DETAIL, cacheModel.getValue(), cacheModel.getUserId());
            cacheService.updateExpired(code, SysConstants.RedisTime.PRODUCT_POPULARIZE);
            return "redirect:" + url;
        } else if (SysConstants.ShareCacheModelType.PRODUCT_DETAIL == cacheModel.getType()) { // 产品详情页
            ProductReqVO reqVO = new ProductReqVO();
            reqVO.setId(cacheModel.getValue());
            reqVO.setStatus(SysConstants.ProductStatus.SELLING);
            Product product = productService.getDetail(reqVO);
            cacheService.updateExpired(code, SysConstants.RedisTime.PRODUCT_POPULARIZE);
            if (product != null) {
                // 增加产品浏览记录
                productPreviewService.saveOne(CommonUtils.getIpAddr(request), cacheModel.getValue(), cacheModel.getUserId());
                // 省列表
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("level", 1);
                model.addAttribute("provinces", areaService.list(wrapper));
                // 取该产品所有属性里面最多的库存作为该产品的库存展示
                if (product.getProperties() != null && !product.getProperties().isEmpty())
                    for (ProdProperty property : product.getProperties()) {
                        if (property.getItems() != null && !property.getItems().isEmpty())
                            for (ProdPropertyItem item : property.getItems()) {
                                if (product.getRemain() < item.getRemain())
                                    product.setRemain(item.getRemain());
                            }
                    }
            }
            model.addAttribute("product", product);
            model.addAttribute("kfPhone", kfPhone);
            return "product/detail";
        }
        return "redirect:/";
    }

    @HasRole(SysConstants.Role.ADMIN)
    @GetMapping("/uploadFile2oss")
    public String uploadFile2oss() {
        return "user/uploadFile2oss";
    }

    // 举报
    @GetMapping("/report")
    public String report() {
        return "report";
    }

    @GetMapping("/report2")
    public String report2() {
        return "report2";
    }

    @GetMapping("/report3")
    public String report3() {
        return "report3";
    }

    @GetMapping("/page/{id}")
    public String customPage(@PathVariable String id, Model model) {
        if (StringUtils.isEmpty(id))
            return "404";
        try {
            MyPage page = myPageService.getById(Long.parseLong(id));
            if (page == null)
                return "404";
            model.addAttribute("page", page);
            return "pages/template";
        } catch (Exception e) {
            return "404";
        }
    }

    private Cookie getCookieByKey(HttpServletRequest request, String key) {
        if (StringUtils.isEmpty(key))
            return null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key))
                    return cookie;
            }
        }
        return null;
    }

    private void addCookie(HttpServletResponse response, String key, String value, int maxAge) {
        if (!StringUtils.isEmpty(key)) {
            if (value == null)
                value = "";
            Cookie cookie = new Cookie(key, value);
            if (maxAge > 0)
                cookie.setMaxAge(maxAge);
            response.addCookie(cookie);
        }
    }

    private void removeCookie(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie cookie = getCookieByKey(request, key);
        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            response.addCookie(cookie);
        }
    }
}
