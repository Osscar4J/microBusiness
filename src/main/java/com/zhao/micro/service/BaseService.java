package com.zhao.micro.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhao.micro.reqvo.BaseReqVO;

public interface BaseService<T> extends IService<T> {

    IPage<T> getPage(BaseReqVO reqVO);

    IPage<T> getPage();

    T getDetail(BaseReqVO reqvo);
}
