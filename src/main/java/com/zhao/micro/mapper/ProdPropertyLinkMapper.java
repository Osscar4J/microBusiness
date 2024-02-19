package com.zhao.micro.mapper;

import com.zhao.micro.entity.ProdPropertyLink;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdPropertyLinkMapper extends MyBaseMapper<ProdPropertyLink> {

    /**
     * 查询指定产品的库存（各个属性的库存信息）
     *
     * @param prodId
     * @return
     */
    @Select("SELECT self.*, ppl.name as propertyName " +
            "FROM prod_property_lk_tb self " +
            "LEFT JOIN prod_property_item_tb ppl on ppl.id=self.property_item_id " +
            "WHERE self.prod_id=#{prodId} " +
            "ORDER BY ppl.id")
    List<ProdPropertyLink> selectRemainByProdId(@Param("prodId") Long prodId);
}
