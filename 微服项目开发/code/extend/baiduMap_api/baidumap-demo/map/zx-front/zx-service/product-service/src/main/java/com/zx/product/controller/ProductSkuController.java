package com.zx.product.controller;

import com.zx.domain.entity.product.ProductSku;
import com.zx.product.service.ProductSkuService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("productSku")
public class ProductSkuController {
    @Resource
    private ProductSkuService productSkuService;
    @PostMapping("getSkuListBySkuIds")
    public List<ProductSku> getSkuListBySkuIds( @RequestParam("ids") List<Long> ids) {
        return productSkuService.getSkuListBySkuIds(ids);
    }
}