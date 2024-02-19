package com.zhao.micro.service.impl;

import com.zhao.micro.entity.Product;
import com.zhao.micro.entity.ProductPreview;
import com.zhao.micro.entity.User;
import com.zhao.micro.mapper.ProductMapper;
import com.zhao.micro.mapper.ProductPreviewMapper;
import com.zhao.micro.service.MyBaseService;
import com.zhao.micro.service.ProductPreviewService;
import com.zhao.micro.service.ProductService;
import com.zhao.micro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ProductPreviewServiceImpl extends MyBaseService<ProductPreviewMapper, ProductPreview> implements ProductPreviewService {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductMapper productMapper;

    @Async
    @Override
    public void saveOne(String ip, Long prodId, Long userId) {
        Product product = productService.getById(prodId);
        if (product != null) {
            productMapper.addClickCount(prodId);
            ProductPreview entity = new ProductPreview();
            entity.setIp(ip);
            entity.setProdId(prodId);
            entity.setSellerId(userId);
            if (userId != null) {
                User user = userService.getById(userId);
                entity.setProxyId(user.getRecommendId());
            }
            this.save(entity);
        }
    }
}
