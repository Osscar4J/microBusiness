package com.zhao.micro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.*;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.model.OrderModel;
import com.zhao.micro.model.PayModel;
import com.zhao.micro.model.ShareCacheModel;
import com.zhao.micro.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service
public abstract class AbstractPayService implements PayService {

    @Value("${order-confirm-url}")
    private String confirmOrderUrl;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProdPropertyItemService prodPropertyItemService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private OrderProdPropertyService orderProdPropertyService;
    @Autowired
    private ProdPropertyLinkService prodPropertyLinkService;

    @Override
    public String getAppid() {
        return null;
    }

    @Override
    public String getAppSecret() {
        return null;
    }

    @Override
    public void updateAppidSecret(PayModel model) {

    }

    @Override
    public void initAppidSecret() {

    }

    @Transactional
    @Override
    public String productOrder(OrderModel model) {
        if (model.getProdId() == null)
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        if (StringUtils.isEmpty(model.getAddr()))
            throw new BusinessException(500, "请填写详细地址");
        if (StringUtils.isEmpty(model.getReceiver()))
            throw new BusinessException(500, "请填写收件人姓名");
        if (StringUtils.isEmpty(model.getPhone()) || !model.getPhone().matches("^1[345789]\\d{9}$"))
            throw new BusinessException(500, "请填写正确的手机号码");
        if (model.getProperties() == null || model.getProperties().isEmpty())
            throw new BusinessException(500, "请选择产品属性");
        Product product = productService.getById(model.getProdId());

        User customer = userService.getCustomerByPhone(model.getPhone(), model.getReceiver(), model.getsId(), model.getAddr());

        Order order = new Order();
        order.setOrderNo(orderService.generateOrderNo());
        order.setName(product.getName());
        StringBuilder sb = new StringBuilder();
        OrderProdProperty orderProdProperty;
        for (ProdPropertyItem pItem : model.getProperties()) {
            pItem = prodPropertyItemService.getById(pItem.getId());

            // 查看库存
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("prod_id", product.getId());
            wrapper.eq("property_item_id", pItem.getId());
            ProdPropertyLink link = prodPropertyLinkService.getOne(wrapper);
            if (link.getRemain() < model.getCount())
                throw new BusinessException(ResponseStatus.PRODUCT_REMAIN_EMPTY);

            sb.append(pItem.getName());
            sb.append("、");
            // 保存订单跟用户所选的产品属性的关联关系
            orderProdProperty = new OrderProdProperty();
            orderProdProperty.setOrderNo(order.getOrderNo());
            orderProdProperty.setPropertyId(pItem.getId());
            orderProdProperty.setPropertyName(pItem.getName());
            orderProdPropertyService.save(orderProdProperty);
        }
        order.setProdProperty(sb.toString());
        order.setProdProperty(order.getProdProperty().substring(0, order.getProdProperty().length() - 1));
        order.setProdId(model.getProdId());
        order.setStatus(SysConstants.OrderStatus.NOT_PAY);
        order.setMark(model.getMark());

        order.setPayStyle(SysConstants.PayStyle.WX_PAY);
        order.setProdCount(model.getCount());
        order.setTotalFee(model.getCount() * product.getPrice());
        order.setUserId(customer.getId());
        ShareCacheModel cacheModel = (ShareCacheModel) cacheService.get(model.getCacheCode());
        if (cacheModel == null)
            throw new BusinessException(500, "请求异常，请刷新重试！");
        User seller = userService.getById(cacheModel.getUserId());
        order.setProxyId(seller.getRecommendId());
        order.setSellerId(cacheModel.getUserId());
        orderService.save(order);
        return confirmOrderUrl + "?o=" + order.getOrderNo();
    }
}
