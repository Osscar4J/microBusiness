package com.zhao.micro.service;

import com.zhao.micro.entity.ProdProperty;
import com.zhao.micro.reqvo.BaseReqVO;

import java.util.List;

public interface ProdPropertyService extends BaseService<ProdProperty> {

    List<ProdProperty> getListWithItem(BaseReqVO reqVO);

}
