package com.zhao.micro.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.micro.annotation.HasRole;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.MyPage;
import com.zhao.micro.respvo.BaseResponse;
import com.zhao.micro.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@HasRole(SysConstants.Role.ADMIN)
@RestController
@RequestMapping("/api/page")
public class MyPageApi {

    @Autowired
    private MyPageService myPageService;

    @GetMapping
    public BaseResponse list() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.ne("status", SysConstants.DELETED);
        wrapper.orderByDesc("create_time");
        return BaseResponse.SUCCESS(myPageService.list(wrapper));
    }

    @PostMapping
    public BaseResponse saveOrUpdate(@RequestBody MyPage entity) {
        return BaseResponse.SUCCESS(myPageService.saveOrUpdate(entity));
    }

    @DeleteMapping("/{id}")
    public BaseResponse remove(@PathVariable long id) {
        return BaseResponse.SUCCESS(myPageService.removeById(id));
    }
}
