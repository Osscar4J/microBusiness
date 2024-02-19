package com.zhao.micro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.entity.*;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.mapper.ProdPropertyLinkMapper;
import com.zhao.micro.mapper.ProductMapper;
import com.zhao.micro.model.PorductSaleModel;
import com.zhao.micro.reqvo.BaseReqVO;
import com.zhao.micro.service.MyBaseService;
import com.zhao.micro.service.ProdPicService;
import com.zhao.micro.service.ProdPropertyLinkService;
import com.zhao.micro.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl extends MyBaseService<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProdPicService prodPicService;
    @Autowired
    private ProdPropertyLinkService prodPropertyLinkService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProdPropertyLinkMapper prodPropertyLinkMapper;

    @Override
    public PorductSaleModel getPreviewAndSaleCount(Long sellerId, Long prodId) {
        if (sellerId == null || prodId == null)
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        return productMapper.selectPreviewAndSaleCount(sellerId, prodId);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        if (id == null || status == null)
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("id", id);
        wrapper.set("status", status);
        return this.update(wrapper);
    }

    @Override
    public List<ProdPropertyLink> getRemainByProdId(Long prodId) {
        if (prodId == null)
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        return prodPropertyLinkMapper.selectRemainByProdId(prodId);
    }

    @Transactional
    @Override
    public boolean updateRemain(List<ProdPropertyLink> items) {
        if (items.isEmpty())
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        boolean re = false;
        UpdateWrapper wrapper;
        for (ProdPropertyLink item : items) {
            wrapper = new UpdateWrapper();
            wrapper.eq("prod_id", item.getProdId());
            wrapper.eq("property_id", item.getPropertyId());
            wrapper.eq("property_item_id", item.getPropertyItemId());
            wrapper.set("remain", item.getRemain());
            re = prodPropertyLinkService.update(wrapper);
        }
        return re;
    }

    @Transactional
    @Override
    public boolean saveOrUpdate(Product entity) {
        if (entity.getId() == null)
            return this.save(entity);
        return this.updateById(entity);
    }

    @Transactional
    @Override
    public boolean updateById(Product entity) {
        if (super.updateById(entity)) {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("prod_id", entity.getId());
            prodPicService.remove(wrapper);
            this.saveBanners(entity);
            prodPropertyLinkService.remove(wrapper);
            this.saveProperties(entity);
            return true;
        }
        return false;
    }

    private void saveBanners(Product entity) {
        if (entity.getBanners() != null && !entity.getBanners().isEmpty()) {
            for (ProdPic banner : entity.getBanners()) {
                banner.setProdId(entity.getId());
                prodPicService.save(banner);
            }
        }
    }

    private void saveProperties(Product entity) {
        if (entity.getProperties() != null && !entity.getProperties().isEmpty()) {
            ProdPropertyLink link;
            for (ProdProperty property : entity.getProperties()) {
                for (ProdPropertyItem item : property.getItems()) {
                    link = new ProdPropertyLink();
                    link.setProdId(entity.getId());
                    link.setPropertyId(property.getId());
                    link.setCreateBy(entity.getCreateBy());
                    link.setRemain(item.getRemain());
                    link.setPropertyItemId(item.getId());
                    link.setName(item.getName());
                    prodPropertyLinkService.save(link);
                }
            }
        }
    }

    @Transactional
    @Override
    public boolean save(Product entity) {
        if (super.save(entity)) {
            // 保存banner图
            this.saveBanners(entity);
            // 保存商品属性
            this.saveProperties(entity);
            return true;
        }
        return false;
    }

    @Override
    public Product getDetail(BaseReqVO reqvo) {
        return productMapper.selectDetail(reqvo);
    }
}
