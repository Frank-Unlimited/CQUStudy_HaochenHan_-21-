package com.zx.product.controller;


import com.zx.domain.entity.product.Brand;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.common.ResultCodeEnum;
import com.zx.product.service.BrandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableCaching
@Tag(name = "品牌相关接口")
@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @GetMapping("/findAll")
    public Result<List<Brand>> findAll(){
        List<Brand> list = brandService.list();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }
}
