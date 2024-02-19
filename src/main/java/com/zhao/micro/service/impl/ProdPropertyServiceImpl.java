package com.zhao.micro.service.impl;

import com.zhao.micro.entity.ProdProperty;
import com.zhao.micro.entity.ProdPropertyItem;
import com.zhao.micro.mapper.ProdPropertyMapper;
import com.zhao.micro.reqvo.BaseReqVO;
import com.zhao.micro.service.MyBaseService;
import com.zhao.micro.service.ProdPropertyItemService;
import com.zhao.micro.service.ProdPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdPropertyServiceImpl extends MyBaseService<ProdPropertyMapper, ProdProperty> implements ProdPropertyService {

    @Autowired
    private ProdPropertyMapper prodPropertyMapper;
    @Autowired
    private ProdPropertyItemService prodPropertyItemService;

    @Override
    public List<ProdProperty> getListWithItem(BaseReqVO reqVO) {
        return prodPropertyMapper.selectListWithItem(reqVO);
    }

    @Transactional
    @Override
    public boolean save(ProdProperty entity) {
        if (super.save(entity)) {
            if (entity.getItems() != null && !entity.getItems().isEmpty()) {
                for (ProdPropertyItem item : entity.getItems()) {
                    item.setPropertyId(entity.getId());
                    item.setCreataeBy(entity.getCreateBy());
                    prodPropertyItemService.save(item);
                }
            }
            return true;
        }
        return false;
    }
}
