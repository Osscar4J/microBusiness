package com.zhao.micro.mapper;

import com.zhao.micro.entity.Product;
import com.zhao.micro.model.PorductSaleModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMapper extends MyBaseMapper<Product> {

    /**
     * 浏览量+1
     *
     * @param prodId
     * @return
     */
    @Update("update product_tb set click_count=click_count+1 where id=#{prodId}")
    int addClickCount(@Param("prodId") Long prodId);

    /**
     * 销量+1
     *
     * @param prodId 产品id
     * @param count  要增加的数量
     * @return
     */
    @Update("update product_tb set act_sell_count=act_sell_count+#{count}, sell_count=sell_count+#{count} where id=#{prodId}")
    int addSellCount(@Param("prodId") Long prodId, @Param("count") int count);

    /**
     * 查询指定员工对于指定商品的所宣传的浏览量和客户购买量
     *
     * @param sellerId 员工id
     * @param prodId   产品id
     * @return
     */
    @Select("SELECT COUNT(self.id) viewCount, oc.orderCount orderCount " +
            "FROM `product_preview_tb` self " +
            "LEFT JOIN ( " +
            "SELECT o.prod_id, COUNT(o.order_no) orderCount FROM order_tb o WHERE o.`status`=1 and o.seller_id=#{sellerId} GROUP BY o.prod_id " +
            ") oc on oc.prod_id=self.prod_id " +
            "WHERE self.seller_id=#{sellerId} and self.prod_id=#{prodId} " +
            "GROUP BY oc.orderCount")
    PorductSaleModel selectPreviewAndSaleCount(@Param("sellerId") long sellerId, @Param("prodId") long prodId);
}
