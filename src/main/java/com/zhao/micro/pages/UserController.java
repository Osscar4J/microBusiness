package com.zhao.micro.pages;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.micro.annotation.CurrentUser;
import com.zhao.micro.annotation.HasRole;
import com.zhao.micro.annotation.LoginRequired;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.User;
import com.zhao.micro.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@LoginRequired
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MyPageService myPageService;
    @Autowired
    private UserQrcodeService userQrcodeService;

    @Qualifier("xorpayService")
    @Autowired
    private PayService payService;

    @GetMapping("/index")
    public String index(Model model, @CurrentUser User user) {
        if (user.getRoleId() == SysConstants.Role.MICRO_BUSINESS)
            return "user/business/index";

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.ne("status", SysConstants.DELETED);
        model.addAttribute("userCount", userService.count(wrapper));

        wrapper.eq("role_id", SysConstants.Role.PROXY);
        model.addAttribute("proxyCount", userService.count(wrapper));

        wrapper = new QueryWrapper();
        wrapper.ne("status", SysConstants.DELETED);
        wrapper.eq("role_id", SysConstants.Role.MICRO_BUSINESS);

        long amount = 0L;
        if (user.getRoleId() == SysConstants.Role.ADMIN) {
            amount = orderService.getSumByStatus(SysConstants.OrderStatus.PAYED, null, null);
        } else if (user.getRoleId() == SysConstants.Role.PROXY) {
            amount = orderService.getSumByStatus(SysConstants.OrderStatus.PAYED, user.getId(), null);
            wrapper.eq("recommend_id", user.getId());
        }
        model.addAttribute("microCount", userService.count(wrapper));

        model.addAttribute("amount", amount);
        return "user/index";
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/manager/login";
    }

    /**
     * 修改密码
     *
     * @return
     */
    @GetMapping("/setting")
    public String setting() {
        return "user/setting";
    }

    /**
     * 个人设置
     *
     * @return
     */
    @GetMapping("/profile")
    public String profile() {
        return "user/profile";
    }

    /**
     * 用户管理
     *
     * @return
     */
    @GetMapping("/users")
    public String users() {
        return "user/users";
    }

    /**
     * 域名管理
     *
     * @return
     */
    @HasRole({SysConstants.Role.ADMIN})
    @GetMapping("/domain")
    public String domains() {
        return "user/domain";
    }

    /**
     * 商品管理
     *
     * @return
     */
    @HasRole({SysConstants.Role.ADMIN})
    @GetMapping("/products")
    public String products() {
        return "user/products";
    }

    /**
     * 员工业绩
     *
     * @return
     */
    @GetMapping("/performance")
    public String performance() {
        return "user/userPerformance";
    }

    /**
     * 订单管理
     *
     * @return
     */
    @GetMapping("/order")
    public String orders() {
        return "user/orders";
    }

    /**
     * 页面配置（公众号页面、支付成功后的公众号页面等）
     *
     * @return
     */
    @GetMapping("/page")
    public String pageSetting() {
        return "user/pageSetting";
    }

    /**
     * 编辑页面
     *
     * @param id    页面id
     * @param model
     * @return
     */
    @GetMapping("/page/editor")
    public String pageEditor(Long id, Model model) {
        if (id != null) {
            model.addAttribute("page", myPageService.getById(id));
        }
        return "pages/editor";
    }

    /**
     * 微信菜单
     *
     * @return
     */
    @GetMapping("/wechat")
    public String wechatMenus() {
        return "user/wechat";
    }

    /**
     * 账号安全
     *
     * @return
     */
    @GetMapping("/safe")
    public String accountSfe(Model model) {
        model.addAttribute("appid", payService.getAppid());
        model.addAttribute("appsecret", payService.getAppSecret());
        return "user/safe";
    }

    /**
     * 用户的微信码管理页面
     *
     * @param id 活码id
     * @return
     */
    @GetMapping("/wxqrcode")
    public String wxQrCodes(long id, Model model) {
        model.addAttribute("qrcode", userQrcodeService.getById(id));
        return "user/wxQrcodes";
    }
}
