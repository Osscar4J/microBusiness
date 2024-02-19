package com.zhao.micro.mapper;

import com.zhao.micro.entity.WxQrCode;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface WxQrCodeMapper extends MyBaseMapper<WxQrCode> {

    /**
     * 通过阈值查询微信码
     *
     * @param qrcodeId 活码id
     * @return
     */
    @Select("SELECT self.* from wx_qrcode_tb self where self.scan_times<self.change_limit and self.qrcode_id=#{qrcodeId} limit 1")
    WxQrCode selectByChangeLimit(@Param("qrcodeId") long qrcodeId);

    @Update("UPDATE wx_qrcode_tb set scan_times=scan_times+1 WHERE id=#{id}")
    int updateScanTimes(@Param("id") long id);

}
