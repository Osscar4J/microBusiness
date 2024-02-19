package com.zhao.micro.service.impl;

import com.zhao.micro.entity.MyPage;
import com.zhao.micro.mapper.MyPageMapper;
import com.zhao.micro.service.MyBaseService;
import com.zhao.micro.service.MyPageService;
import org.springframework.stereotype.Service;

@Service
public class MyPageServiceImpl extends MyBaseService<MyPageMapper, MyPage> implements MyPageService {

}
