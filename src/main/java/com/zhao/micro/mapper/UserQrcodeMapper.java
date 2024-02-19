package com.zhao.micro.mapper;

import com.zhao.micro.entity.UserQrcode;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserQrcodeMapper extends MyBaseMapper<UserQrcode> {

    @Select("select self.*, wq.wxCount " +
            "from user_qrcode_tb self " +
            "LEFT JOIN (" +
            " SELECT wq.qrcode_id qrId, COUNT(wq.id) wxCount FROM wx_qrcode_tb wq GROUP BY qrId " +
            ") wq on wq.qrId=self.id " +
            "where self.status!=-1 and self.user_id=#{userId}")
    List<UserQrcode> selectByUserId(@Param("userId") Long userId);
}
