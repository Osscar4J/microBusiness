package com.zhao.micro.service;

import com.zhao.micro.entity.ProdPropertyLink;
import com.zhao.micro.entity.Product;
import com.zhao.micro.model.PorductSaleModel;

import java.util.List;

public interface ProductService extends BaseService<Product> {

    PorductSaleModel getPreviewAndSaleCount(Long sellerId, Long prodId);

    boolean updateStatus(Long id, Integer status);

    /**
     * 查询指定产品的库存
     *
     * @param prodId 产品id
     * @return
     */
    List<ProdPropertyLink> getRemainByProdId(Long prodId);

    /**
     * 保存产品的库存信息
     *
     * @param items
     * @return
     */
    boolean updateRemain(List<ProdPropertyLink> items);
}
