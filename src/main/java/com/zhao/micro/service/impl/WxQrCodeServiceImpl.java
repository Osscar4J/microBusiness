package com.zhao.micro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.WxQrCode;
import com.zhao.micro.mapper.WxQrCodeMapper;
import com.zhao.micro.service.MyBaseService;
import com.zhao.micro.service.WxQrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WxQrCodeServiceImpl extends MyBaseService<WxQrCodeMapper, WxQrCode> implements WxQrCodeService {

    @Autowired
    private WxQrCodeMapper wxQrCodeMapper;

    @Override
    public List<WxQrCode> getByQrcodeId(long id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("qrcode_id", id);
        wrapper.ne("status", SysConstants.DELETED);
        return this.list(wrapper);
    }

    @Override
    public WxQrCode getRandomByQrcodeId(long qrId) {
        List<WxQrCode> list = this.getByQrcodeId(qrId);
        if (!list.isEmpty()) {
            return list.get((int) Math.floor(Math.random() * list.size()));
        }
        return null;
    }

    @Override
    public WxQrCode getByChangeLimit(long qrId) {
        return wxQrCodeMapper.selectByChangeLimit(qrId);
    }

    @Override
    public boolean updateScanTimes(long id) {
        return wxQrCodeMapper.updateScanTimes(id) > 0;
    }
}
