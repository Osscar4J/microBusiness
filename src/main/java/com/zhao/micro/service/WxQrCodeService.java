package com.zhao.micro.service;

import com.zhao.micro.entity.WxQrCode;

import java.util.List;

public interface WxQrCodeService extends BaseService<WxQrCode> {

    /**
     * 查询微信二维码列表
     *
     * @param id 活码id
     * @return
     */
    List<WxQrCode> getByQrcodeId(long id);

    /**
     * 随机获取一个活码的微信码
     *
     * @param qrId 活码id
     * @return
     */
    WxQrCode getRandomByQrcodeId(long qrId);

    /**
     * 通过阈值查询一个微信码
     *
     * @param qrId 活码id
     * @return
     */
    WxQrCode getByChangeLimit(long qrId);

    /**
     * 增加扫码次数
     *
     * @param id
     * @return
     */
    boolean updateScanTimes(long id);
}
