package com.zhao.micro.pages;

import com.zhao.micro.annotation.HasRole;
import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.reqvo.OrderReqVO;
import com.zhao.micro.service.OrderService;
import com.zhao.micro.service.PayService;
import com.zhao.micro.service.PoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Value("${auth-redirect-domain}")
    private String authRedirectDomain;
    @Autowired
    private PoiService poiService;
    @Qualifier("xorpayService")
    @Autowired
    private PayService payService;

    /**
     * 查询订单页面
     *
     * @param code
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/query")
    public String query(HttpServletRequest request, String code) throws UnsupportedEncodingException {
//        if (code == null)
//            return toGetWxAuthCodeRedirect(URLEncoder.encode(authRedirectDomain + "/order/query", "utf8"));
        request.getSession().setAttribute("code", code);
        return "order/query";
    }

//    /**
//     * 确认订单页面，XOR pay
//     *
//     * @param request
//     * @param model
//     * @param orderNo
//     * @param openid
//     * @return
//     * @throws UnsupportedEncodingException
//     */
//    @GetMapping("/confirm")
//    public String orderConfirm(HttpServletRequest request, Model model,
//                               @RequestParam(value = "o", required = false) String orderNo,
//                               String openid) throws UnsupportedEncodingException {
//        if (openid == null)
//            return toGetWxOpenidByXorPay(orderNo);
//        request.getSession().setAttribute(SysConstants.SESSION_OPENID, openid);
//        OrderReqVO reqVO = new OrderReqVO();
//        reqVO.setOrderNo(orderNo);
//        model.addAttribute("order", orderService.getDetail(reqVO));
//        return "order/confirm";
//    }

    /**
     * 确认订单页面，微信支付
     * @return
     */
    @GetMapping("/confirm")
    public String orderConfirm(HttpServletRequest request, Model model,
                               @RequestParam(value="o",required = false) String orderNo,
                               String code) throws UnsupportedEncodingException {
        if (code == null)
            return toGetWxAuthCode(orderNo);

        String authRes = HttpClient.doGet("https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=" + SysConstants.GH_APPID +
                "&secret=" + SysConstants.GH_APP_SECRET +
                "&code=" + code +
                "&grant_type=authorization_code");
        JSONObject authResJson = JSONObject.parseObject(authRes);
        String openid = authResJson.getString("openid");
        if (StringUtils.isEmpty(openid))
            return toGetWxAuthCode(orderNo);

        request.getSession().setAttribute(SysConstants.SESSION_OPENID, openid);
        OrderReqVO reqVO = new OrderReqVO();
        reqVO.setOrderNo(orderNo);
        model.addAttribute("order", orderService.getDetail(reqVO));
        return "order/confirm";
    }

    /**
     * 导出订单信息
     *
     * @param st 开始时间, 格式: yyyy-MM-dd hh:mm:ss
     * @param et 结束时间, 格式: yyyy-MM-dd hh:mm:ss
     * @return
     * @throws IOException
     */
    @HasRole(SysConstants.Role.ADMIN)
    @GetMapping("/export")
    public void exportOrderStat(HttpServletResponse response, String st, String et) throws IOException {
        if (org.springframework.util.StringUtils.isEmpty(st) || org.springframework.util.StringUtils.isEmpty(et))
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        OrderReqVO reqVO = new OrderReqVO();
        reqVO.setSt(st);
        reqVO.setEt(et);
        poiService.exportOrder(response, reqVO);
    }

    private String toGetWxAuthCode(String orderNo) throws UnsupportedEncodingException {
        String redirectUri = URLEncoder.encode(authRedirectDomain + "/order/confirm?o=" + orderNo, "utf8");
        return toGetWxAuthCodeRedirect(redirectUri);
    }

    private String toGetWxOpenidByXorPay(String orderNo) throws UnsupportedEncodingException {
        String redirectUri = URLEncoder.encode(authRedirectDomain + "/order/confirm?o=" + orderNo, "utf8");
        return "redirect:https://xorpay.com/api/openid/" + payService.getAppid() +
                "?callback=" + redirectUri;
    }

    private String toGetWxAuthCodeRedirect(String redirectUri) {
        return "redirect:" +
                "https://open.weixin.qq.com/connect/oauth2/authorize" +
                "?appid=" + SysConstants.GH_APPID +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=snsapi_base&state=1&connect_redirect=1#wechat_redirect";
    }
}
