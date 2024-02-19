package com.zhao.micro.api;

import com.zhao.micro.annotation.CurrentUser;
import com.zhao.micro.annotation.LoginRequired;
import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.entity.User;
import com.zhao.micro.entity.WxQrCode;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.respvo.BaseResponse;
import com.zhao.micro.service.QrCodeService;
import com.zhao.micro.service.WxQrCodeService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/qrcode")
public class QrCodeApi {

    private int maxFileSize = 1024 * 1024 * 2;
    @Autowired
    private QrCodeService qrCodeService;
    @Autowired
    private WxQrCodeService wxQrCodeService;

    @GetMapping
    public BaseResponse createQrCode(String content, Integer w, Integer h) {
        if (w == null)
            w = 200;
        if (h == null)
            h = 200;
        return BaseResponse.SUCCESS(qrCodeService.createQrCode(content, w, h));
    }

    /**
     * 保存用户的微信二维码
     *
     * @param entity
     * @return
     */
    @LoginRequired
    @PostMapping("/wechat")
    public BaseResponse saveOrUpdateWxQrcode(@RequestBody WxQrCode entity, @CurrentUser User user) {
        entity.setUserId(user.getId());
        return BaseResponse.SUCCESS(wxQrCodeService.saveOrUpdate(entity));
    }

    /**
     * 删除微信二维码
     *
     * @param id 微信二维码id
     * @return
     */
    @LoginRequired
    @DeleteMapping("/wechat/{id}")
    public BaseResponse removeWxQrcode(@PathVariable long id, @CurrentUser User user) {
        WxQrCode code = wxQrCodeService.getById(id);
        if (code == null)
            return BaseResponse.ERROR("删除失败！");
        if (code.getUserId().longValue() != user.getId().longValue())
            throw new BusinessException(ResponseStatus.NO_PERMISSION);
        return BaseResponse.SUCCESS(wxQrCodeService.removeById(id));
    }

    @GetMapping("/decode")
    public BaseResponse decode(String url) {
        return BaseResponse.SUCCESS(qrCodeService.decode(url));
    }

    @PostMapping("/decode")
    public BaseResponse decode(@Param("file") MultipartFile file) throws IOException {
        if (file.isEmpty())
            return BaseResponse.ERROR("缺少文件");
        if (file.getSize() > maxFileSize)
            return BaseResponse.ERROR("请选择大小2M以内的文件");
        return BaseResponse.SUCCESS(qrCodeService.decode(file.getInputStream()));
    }
}
