package com.zhao.micro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhao.micro.entity.ProdPropertyLink;
import com.zhao.micro.mapper.ProdPropertyMapper;
import com.zhao.micro.service.MyBaseService;
import com.zhao.micro.service.OrderProdPropertyService;
import com.zhao.micro.service.ProdPropertyLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdPropertyLinkServiceImpl extends MyBaseService<ProdPropertyMapper, ProdPropertyLink> implements ProdPropertyLinkService {

    @Autowired
    private OrderProdPropertyService orderProdPropertyService;

    @Async
    @Override
    public void minusRemainByOrder(String orderNo, Long prodId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("order_no", orderNo);
        wrapper.select("property_id");
        List<Long> itemIds = orderProdPropertyService.listObjs(wrapper, o -> o);
        if (!itemIds.isEmpty()) {
            wrapper = new QueryWrapper();
            wrapper.eq("prod_id", prodId);
            wrapper.in("property_item_id", itemIds);
            synchronized (this) {
                List<ProdPropertyLink> links = this.list(wrapper);
                if (!links.isEmpty()) {
                    for (ProdPropertyLink link : links) {
                        UpdateWrapper wrapper1 = new UpdateWrapper();
                        wrapper1.eq("prod_id", prodId);
                        wrapper1.eq("property_item_id", link.getPropertyItemId());
                        wrapper1.set("remain", link.getRemain() - 1);
                        this.update(wrapper1);
                    }
                }
            }
        }
    }
}
