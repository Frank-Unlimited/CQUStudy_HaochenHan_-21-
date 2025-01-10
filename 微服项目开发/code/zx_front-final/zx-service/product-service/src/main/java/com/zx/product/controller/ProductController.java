package com.zx.product.controller;


import com.alipay.api.domain.Product;
import com.github.pagehelper.PageInfo;
import com.zx.domain.dto.h5.ProductSkuDto;
import com.zx.domain.entity.product.ProductSku;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.common.ResultCodeEnum;
import com.zx.domain.vo.h5.ProductItemVo;
import com.zx.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.*;

@EnableCaching
@RestController
@RequestMapping("/product")
@Tag(name = "商品管理模块")
//@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Operation(summary = "商品列表接口")
    @GetMapping("/{pageNum}/{pageSize}")
    //PageInfo是PageHelper的类
    public Result<PageInfo<ProductSku>> findByPage(@PathVariable Integer pageNum,
                                                   @PathVariable Integer pageSize,
                                                   ProductSkuDto dto) {
        PageInfo<ProductSku> pageInfo = productService.findByPage(pageNum, pageSize, dto);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "商品详情")
    @GetMapping("/item/{skuId}")
    public Result<ProductItemVo> item(@PathVariable Long skuId) {
        ProductItemVo productItemVo = productService.item(skuId);
        return Result.build(productItemVo , ResultCodeEnum.SUCCESS);
    }


}
