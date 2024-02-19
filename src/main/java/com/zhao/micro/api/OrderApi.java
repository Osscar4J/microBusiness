package com.zhao.micro.api;

import com.zhao.micro.annotation.CommonPath;
import com.zhao.micro.annotation.CurrentUser;
import com.zhao.micro.annotation.HasRole;
import com.zhao.micro.annotation.LoginRequired;
import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.Area;
import com.zhao.micro.entity.Order;
import com.zhao.micro.entity.User;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.model.OrderCountModel;
import com.zhao.micro.model.OrderModel;
import com.zhao.micro.pay.wx.MyWXPayConfig;
import com.zhao.micro.pay.wx.WXPay;
import com.zhao.micro.pay.wx.WXPayUtil;
import com.zhao.micro.reqvo.OrderReqVO;
import com.zhao.micro.respvo.BaseResponse;
import com.zhao.micro.service.AreaService;
import com.zhao.micro.service.OrderService;
import com.zhao.micro.service.PayService;
import com.zhao.micro.utils.CommonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@LoginRequired
@RestController
@RequestMapping("/api/order")
public class OrderApi {

    private Logger logger = Logger.getLogger(OrderApi.class);
    @Autowired
    private OrderService orderService;
    @Autowired
//    @Qualifier("xorpayService")
    @Qualifier("wxpayService")
    private PayService payService;
    @Autowired
    private AreaService areaService;

    @GetMapping
    public BaseResponse page(OrderReqVO reqVO, @CurrentUser User user) {
        if (user.getRoleId() != SysConstants.Role.ADMIN) {
            reqVO.setSellerId(user.getId());
            reqVO.setStatus(SysConstants.OrderStatus.PAYED);
        }
        return BaseResponse.SUCCESS(orderService.getPage(reqVO));
    }

    /**
     * 查询最近一个月的营业额统计
     *
     * @return
     */
    @GetMapping("/amountStat")
    public BaseResponse getAmountStat(@CurrentUser User user) {
        Calendar calendar = Calendar.getInstance();
        Date et = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 30);
        Long proxyId = null;
        if (user.getRoleId() != SysConstants.Role.ADMIN)
            proxyId = user.getId();
        return BaseResponse.SUCCESS(orderService.getAmountList(calendar.getTime(), et, proxyId));
    }

    /**
     * 调微信接口查询订单状态
     *
     * @param orderNo 本地订单号
     * @return
     */
    @CommonPath
    @GetMapping("/status")
    public BaseResponse queryStatus(String orderNo) {
        return BaseResponse.SUCCESS(orderService.queryOrder(orderNo));
    }

    /**
     * 统一下单
     *
     * @return
     */
    @CommonPath
    @GetMapping("/unified")
    public BaseResponse unifiedOrder(HttpServletRequest request, String orderNo) {
        if (StringUtils.isEmpty(orderNo))
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);

        Order order = orderService.getById(orderNo);
        if (order == null)
            throw new BusinessException(ResponseStatus.OBJECT_NOT_FOUND);

        return BaseResponse.SUCCESS(payService.unifiedorder(order.getName(),
                orderNo,
                order.getTotalFee(),
                SysConstants.WXPayTradeType.JSAPI,
                order.getProdId(),
                CommonUtils.getIpAddr(request),
                (String) request.getSession().getAttribute(SysConstants.SESSION_OPENID),
                null));
    }

    /**
     * 提交订单，返回确认订单页面（在该页面发起支付）
     *
     * @param request
     * @param model
     * @param refer
     * @return
     */
    @CommonPath
    @PostMapping("/submit")
    public BaseResponse submitOrder(HttpServletRequest request, @RequestBody OrderModel model, @RequestHeader("Referer") String refer) {
        if (StringUtils.isEmpty(refer))
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        model.setCacheCode(refer.substring(refer.lastIndexOf("/") + 1));
        model.setUserIp(CommonUtils.getIpAddr(request));
        return BaseResponse.SUCCESS(payService.productOrder(model));
    }

    /**
     * 支付回调
     *
     * @return
     */
    @CommonPath
    @RequestMapping("/callback")
    public String payCallback(HttpServletRequest request) {
        try {
            String result = CommonUtils.getParamtersStrByRequest(request);// 获取微信的返回信息
            logger.info("支付回调：{}" + result);
            Map<String, String> map = WXPayUtil.xmlToMap(result);
            WXPay wxpay = new WXPay(new MyWXPayConfig());
            // 验证签名
            if (wxpay.isResponseSignatureValid(map)) {
                return payService.payResultNotify(map);
            } else {
                return "签名不正确";
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return PayService.FAIL;
    }

    /**
     * 查询指定用户的订单数量统计（订单总数、已发货、未发货）
     *
     * @param user
     * @return
     */
    @GetMapping("/count")
    public BaseResponse<OrderCountModel> getCountByUser(@CurrentUser User user) {
        return BaseResponse.SUCCESS(orderService.getCountByUserId(user.getId()));
    }

    /**
     * 退款
     *
     * @param entity
     * @return
     */
    @HasRole(SysConstants.Role.ADMIN)
    @PutMapping("/refund")
    public BaseResponse refund(@RequestBody Order entity) {
        if (StringUtils.isEmpty(entity.getOrderNo()))
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        orderService.refund(entity.getOrderNo(), entity.getReason());
        return BaseResponse.SUCCESS();
    }

    @CommonPath
    @GetMapping("/latest")
    public BaseResponse getRandOrder(@RequestHeader("Referer") String referer) {
        if (StringUtils.isEmpty(referer))
            return BaseResponse.SUCCESS();
        List<Area> areas = areaService.getRandCidy(20);
        List<String> res = new ArrayList<>();
        areas.forEach(area -> {
            res.add(area.getName() + createRandPhone() + "抢购成功");
        });
        return BaseResponse.SUCCESS(res);
    }

    /**
     * 查询订单信息
     *
     * @param orderNoOrPhone 订单号
     * @return
     */
    @CommonPath
    @GetMapping("/query")
    public BaseResponse getDetail(@RequestParam("o") String orderNoOrPhone, Integer status, @SessionAttribute(value = "code", required = false) String code) {
        OrderReqVO reqVO = new OrderReqVO();
        reqVO.setOrderNoOrPhone(orderNoOrPhone);
        reqVO.setStatus(status);
        return BaseResponse.SUCCESS(orderService.getDetail(reqVO));
    }

    private int[] phonePrefix = new int[]{3, 5, 8};

    private String createRandPhone() {
        return "1" + phonePrefix[(int) (phonePrefix.length * Math.random())] +
                ((int) (Math.random() * 10)) +
                "****" + (1000 + (int) (8999 * Math.random()));
    }
}
