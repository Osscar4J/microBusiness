package com.zhao.micro.service;

import com.zhao.micro.entity.ProdPropertyLink;

public interface ProdPropertyLinkService extends BaseService<ProdPropertyLink> {

    /**
     * 购买成功后库存-1
     *
     * @param orderNo 订单号
     * @param prodId  产品id
     * @return
     */
    void minusRemainByOrder(String orderNo, Long prodId);
}
