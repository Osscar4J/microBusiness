package com.zhao.micro.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.micro.annotation.CurrentUser;
import com.zhao.micro.annotation.HasRole;
import com.zhao.micro.annotation.LoginRequired;
import com.zhao.micro.constants.ResponseStatus;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.entity.ProdProperty;
import com.zhao.micro.entity.ProdPropertyLink;
import com.zhao.micro.entity.Product;
import com.zhao.micro.entity.User;
import com.zhao.micro.exception.BusinessException;
import com.zhao.micro.reqvo.ProductReqVO;
import com.zhao.micro.reqvo.ProductViewReqVO;
import com.zhao.micro.respvo.BaseResponse;
import com.zhao.micro.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@LoginRequired
@RestController
@RequestMapping("/api/product")
public class ProductApi {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProdPropertyService prodPropertyService;
    @Autowired
    private ProdPropertyItemService prodPropertyItemService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductPreviewService productPreviewService;

    @GetMapping("/viewSale")
    public BaseResponse getViewAndSaleCount(Long prodId, @CurrentUser User user) {
        return BaseResponse.SUCCESS(productService.getPreviewAndSaleCount(user.getId(), prodId));
    }

    @HasRole({SysConstants.Role.ADMIN})
    @GetMapping
    public BaseResponse page(ProductReqVO reqVO) {
        if (!StringUtils.isEmpty(reqVO.getName()))
            reqVO.setName("%" + reqVO.getName() + "%");
        return BaseResponse.SUCCESS(productService.getPage(reqVO));
    }

    /**
     * 商品浏览记录
     *
     * @param reqVO
     * @return
     */
    @GetMapping("/view")
    public BaseResponse viewTimesPage(ProductViewReqVO reqVO, @CurrentUser User user) {
        if (user.getRoleId() == SysConstants.Role.MICRO_BUSINESS) {
            reqVO.setSellerId(user.getId());
        } else if (user.getRoleId() == SysConstants.Role.PROXY) {
            reqVO.setProxyId(user.getId());
        } else if (user.getRoleId() != SysConstants.Role.ADMIN) {
            return BaseResponse.SUCCESS();
        }
        return BaseResponse.SUCCESS(productPreviewService.getPage(reqVO));
    }

    /**
     * 查询已上架的商品列表
     *
     * @param reqVO
     * @return
     */
    @GetMapping("/list")
    public BaseResponse publishedPage(ProductReqVO reqVO) {
        if (!StringUtils.isEmpty(reqVO.getName()))
            reqVO.setName("%" + reqVO.getName() + "%");
        reqVO.setStatus(1);
        return BaseResponse.SUCCESS(productService.getPage(reqVO));
    }

    @HasRole({SysConstants.Role.ADMIN})
    @PostMapping
    public BaseResponse addOrUpdate(@RequestBody Product entity, @CurrentUser User user) {
        if (entity.getId() == null) {
            entity.setCreateBy(user.getId());
        } else {
            entity.setUpdateBy(user.getId());
        }
        return BaseResponse.SUCCESS(productService.saveOrUpdate(entity));
    }

    @HasRole({SysConstants.Role.ADMIN})
    @PutMapping("/status")
    public BaseResponse updateStatus(@RequestBody Product entity) {
        return BaseResponse.SUCCESS(productService.updateStatus(entity.getId(), entity.getStatus()));
    }

    /**
     * 查询商品属性列表
     *
     * @return
     */
    @GetMapping("/property")
    public BaseResponse<List<ProdProperty>> propertyList(ProductReqVO reqVO) {
        if (!StringUtils.isEmpty(reqVO.getName())) {
            reqVO.setName("%" + reqVO.getName() + "%");
        }
        return BaseResponse.SUCCESS(prodPropertyService.getListWithItem(reqVO));
    }

    /**
     * 添加商品属性
     *
     * @param entity
     * @return
     */
    @HasRole({SysConstants.Role.ADMIN})
    @PostMapping("/property")
    public BaseResponse addProperty(@RequestBody ProdProperty entity, @CurrentUser User user) {
        if (StringUtils.isEmpty(entity.getName()))
            throw new BusinessException(ResponseStatus.INVALIDE_PARAMS);
        entity.setCreateBy(user.getId());
        return BaseResponse.SUCCESS(prodPropertyService.save(entity));
    }

    /**
     * 更新商品属性
     *
     * @param entity
     * @return
     */
    @HasRole({SysConstants.Role.ADMIN})
    @PutMapping("/property")
    public BaseResponse updatePropertyStatus(@RequestBody ProdProperty entity) {
        return BaseResponse.SUCCESS(prodPropertyService.updateById(entity));
    }

    /**
     * 查询商品属相选项
     *
     * @param proId 属性id
     * @return
     */
    @GetMapping("/propertyItem")
    public BaseResponse<List<ProdProperty>> propertyItemList(Long proId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("property_id", proId);
        return BaseResponse.SUCCESS(prodPropertyItemService.list(wrapper));
    }

    /**
     * 生成产品推广码
     *
     * @param id 产品id
     * @return
     */
    @GetMapping("/generateQrcode")
    public BaseResponse<String> generateAliveQrcode(Long id, @CurrentUser User user) {
        return BaseResponse.SUCCESS(userService.generateProdPopularizeQrcode(id, user.getId()));
    }

    /**
     * 查询产品的库存
     *
     * @param id 产品id
     * @return
     */
    @GetMapping("/remain/{id}")
    public BaseResponse<List<ProdPropertyLink>> getRemain(@PathVariable Long id) {
        return BaseResponse.SUCCESS(productService.getRemainByProdId(id));
    }

    /**
     * 保存库存信息
     *
     * @param entity
     * @return
     */
    @PutMapping("/remain")
    public BaseResponse saveRemain(@RequestBody Product entity) {
        return BaseResponse.SUCCESS(productService.updateRemain(entity.getRemains()));
    }
}
