package com.zhao.micro.mapper;

import com.zhao.micro.entity.Area;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaMapper extends MyBaseMapper<Area> {

    /**
     * 随机获取城市列表
     *
     * @param count 要获取的数量
     * @return
     */
    @Select("SELECT a.* FROM (SELECT * from area_tb where `level`=2) a ORDER BY RAND() LIMIT #{count}")
    List<Area> selectRandCity(@Param("count") int count);
}
