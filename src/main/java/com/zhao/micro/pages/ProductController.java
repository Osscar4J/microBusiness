package com.zhao.micro.pages;

import com.zhao.micro.annotation.HasRole;
import com.zhao.micro.annotation.LoginRequired;
import com.zhao.micro.constants.SysConstants;
import com.zhao.micro.reqvo.ProductReqVO;
import com.zhao.micro.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@LoginRequired
@RequestMapping("/product")
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @HasRole({SysConstants.Role.ADMIN})
    @GetMapping("/edit")
    public String edit(Model model, Long id) {
        if (id != null) {
            ProductReqVO reqVO = new ProductReqVO();
            reqVO.setId(id);
            model.addAttribute("product", productService.getDetail(reqVO));
        }
        return "product/edit";
    }

    /**
     * 产品推广
     *
     * @return
     */
    @GetMapping("/popularize")
    public String popularize() {
        return "product/popularize";
    }
}
