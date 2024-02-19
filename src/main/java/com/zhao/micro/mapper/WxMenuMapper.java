package com.zhao.micro.mapper;

import com.zhao.micro.entity.WxMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WxMenuMapper extends MyBaseMapper<WxMenu> {

    /**
     * 查询指定状态的所有菜单
     *
     * @param status 如果为null则查询所有
     * @return
     */
    List<WxMenu> selectAllByStatus(@Param("status") Integer status);

}
