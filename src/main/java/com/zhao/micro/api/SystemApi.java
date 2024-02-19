package com.zhao.micro.api;

import com.zhao.micro.annotation.CurrentUser;
import com.zhao.micro.annotation.HasRole;
import com.zhao.micro.annotation.LoginRequired;
import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.User;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.model.PayModel;
import com.zhao.micro.model.SystemModel;
import com.zhao.micro.respvo.BaseResponse;
import com.zhao.micro.service.CacheService;
import com.zhao.micro.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

@LoginRequired
@RestController
@RequestMapping("/api/sys")
public class SystemApi {

    @Autowired
    private CacheService cacheService;
    private String REDIS_KEY_IP_WHITE = "ip-white-";
    @Qualifier("xorpayService")
    @Autowired
    private PayService payService;

    @GetMapping("/ipWhite")
    public BaseResponse getIPWhite(@CurrentUser User user) {
        return BaseResponse.SUCCESS(cacheService.get(REDIS_KEY_IP_WHITE + user.getId()));
    }

    @PostMapping("/ipWhite")
    public BaseResponse saveIPWhite(@CurrentUser User user, @RequestBody SystemModel model) {
        if (StringUtils.isEmpty(model.getIp())) {
            cacheService.remove(REDIS_KEY_IP_WHITE + user.getId());
        } else {
            cacheService.put(REDIS_KEY_IP_WHITE + user.getId(), model.getIp());
        }
        return BaseResponse.SUCCESS();
    }

    /**
     * 设置支付appid  appsecret
     *
     * @param model
     * @return
     */
    @PostMapping("/paySecret")
    public BaseResponse setPayAppidSecret(@RequestBody PayModel model) {
        if (StringUtils.isEmpty(model.getAppid()) || StringUtils.isEmpty(model.getSecret()))
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        payService.updateAppidSecret(model);
        return BaseResponse.SUCCESS();
    }
}
