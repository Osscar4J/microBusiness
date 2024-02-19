package com.zhao.micro.service.impl;

import com.zhao.micro.entity.OrderProdProperty;
import com.zhao.micro.mapper.OrderProdPropertyMapper;
import com.zhao.micro.service.MyBaseService;
import com.zhao.micro.service.OrderProdPropertyService;
import org.springframework.stereotype.Service;

@Service
public class OrderProdPropertyServiceImpl extends MyBaseService<OrderProdPropertyMapper, OrderProdProperty> implements OrderProdPropertyService {

}
