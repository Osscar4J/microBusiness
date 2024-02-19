package com.zhao.micro.mapper;

import com.zhao.micro.entity.Order;
import com.zhao.micro.model.AmountModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderMapper extends MyBaseMapper<Order> {

    Long selectSumByStatus(@Param("status") Integer status, @Param("proxyId") Long proxyId, @Param("sellerId") Long sellerId);

    List<AmountModel> selectAmount(@Param("st") Date st, @Param("et") Date et, @Param("proxyId") Long proxyId);
}
