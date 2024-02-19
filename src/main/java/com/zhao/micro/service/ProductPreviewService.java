package com.zhao.micro.service;

import com.zhao.micro.entity.ProductPreview;

public interface ProductPreviewService extends BaseService<ProductPreview> {

    /**
     * 保存产品浏览记录
     *
     * @param ip     访客ip
     * @param prodId 产品id
     * @param userId 推广该产品的微商的id
     * @return
     */
    void saveOne(String ip, Long prodId, Long userId);
}
