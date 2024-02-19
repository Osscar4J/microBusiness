package com.zhao.micro.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.micro.respvo.BaseResponse;
import com.zhao.micro.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/area")
public class AreaApi {

    @Autowired
    private AreaService areaService;

    @GetMapping
    public BaseResponse list(Long pId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("parent_id", pId);
        return BaseResponse.SUCCESS(areaService.list(wrapper));
    }
}
