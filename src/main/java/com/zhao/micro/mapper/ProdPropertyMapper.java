package com.zhao.micro.mapper;

import com.zhao.micro.entity.ProdProperty;
import com.zhao.micro.reqvo.BaseReqVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdPropertyMapper extends MyBaseMapper<ProdProperty> {

    List<ProdProperty> selectListWithItem(@Param("reqvo") BaseReqVO reqVO);
}
