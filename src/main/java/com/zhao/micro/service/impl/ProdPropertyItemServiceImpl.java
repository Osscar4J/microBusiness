package com.zhao.micro.service.impl;

import com.zhao.micro.entity.ProdPropertyItem;
import com.zhao.micro.mapper.ProdPropertyItemMapper;
import com.zhao.micro.service.MyBaseService;
import com.zhao.micro.service.ProdPropertyItemService;
import org.springframework.stereotype.Service;

@Service
public class ProdPropertyItemServiceImpl extends MyBaseService<ProdPropertyItemMapper, ProdPropertyItem> implements ProdPropertyItemService {

}
