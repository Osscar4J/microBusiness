package com.zhao.micro.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhao.micro.annotation.HasRole;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.Domain;
import com.zhao.micro.reqvo.BaseReqVO;
import com.zhao.micro.reqvo.DomainReqVO;
import com.zhao.micro.respvo.BaseResponse;
import com.zhao.micro.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 域名管理
 */
@HasRole({SysConstants.Role.ADMIN})
@RestController
@RequestMapping("/api/domain")
public class DomainApi {

    @Autowired
    private DomainService domainService;

    @GetMapping
    public BaseResponse<IPage<Domain>> page(DomainReqVO reqVO) {
        return BaseResponse.SUCCESS(domainService.getPage(reqVO));
    }

    @PostMapping
    public BaseResponse saveOrUpdate(@RequestBody Domain entity) {
        return BaseResponse.SUCCESS(domainService.saveOrUpdate(entity));
    }

    /**
     * 更新状态
     *
     * @param entity
     * @return
     */
    @PutMapping("/status")
    public BaseResponse updateStatus(@RequestBody Domain entity) {
        return BaseResponse.SUCCESS(domainService.updateStatus(entity.getId(), entity.getStatus()));
    }
}
