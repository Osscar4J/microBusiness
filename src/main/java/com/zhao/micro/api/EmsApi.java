package com.zhao.micro.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.micro.annotation.HasRole;
import com.zhao.micro.annotation.LoginRequired;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.Ems;
import com.zhao.micro.respvo.BaseResponse;
import com.zhao.micro.service.EmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@LoginRequired
@RestController
@RequestMapping("/api/ems")
public class EmsApi {

    @Autowired
    private EmsService emsService;

    @HasRole(SysConstants.Role.ADMIN)
    @PostMapping
    public BaseResponse save(@RequestBody Ems entity) {
        return BaseResponse.SUCCESS(emsService.save(entity));
    }

    @HasRole(SysConstants.Role.ADMIN)
    @PutMapping
    public BaseResponse update(@RequestBody Ems entity) {
        return BaseResponse.SUCCESS(emsService.updateById(entity));
    }

    /**
     * 通过订单号查询该订单的快递信息
     *
     * @param orderNo 订单号
     * @return
     */
    @GetMapping("/order/{orderNo}")
    public BaseResponse<Ems> getByOrderNo(@PathVariable String orderNo) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("order_no", orderNo);
        wrapper.ne("status", SysConstants.DELETED);
        return BaseResponse.SUCCESS(emsService.getOne(wrapper));
    }
}
