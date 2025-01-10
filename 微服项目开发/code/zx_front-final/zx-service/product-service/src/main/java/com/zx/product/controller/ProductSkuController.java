package com.zx.product.controller;

import com.zx.domain.dto.product.SkuSaleDto;
import com.zx.domain.entity.product.ProductSku;
import com.zx.product.service.ProductSkuService;
import feign.QueryMap;
import jakarta.annotation.Resource;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("productSku")
public class ProductSkuController {
    @Resource
    private ProductSkuService productSkuService;
    @PostMapping("getSkuListBySkuIds")
    public List<ProductSku> getSkuListBySkuIds( @RequestParam("ids") List<Long> ids) {
        return productSkuService.getSkuListBySkuIds(ids);
    }

    @GetMapping("/getProductById/{id}")
    public ProductSku getProductById(@PathVariable("id")Long id){
        return productSkuService.getById(id);
    }

    @GetMapping("/deductInventory/{skuId}/{num}")
    public Boolean deductInventory(@PathVariable("skuId")Long skuId,
                                   @PathVariable("num")Integer num){
        return productSkuService.deductInventory(skuId,num);
    }

    @PostMapping("deductInventories")
    public Boolean deductInventories(@RequestParam("skuIds") List<Long> skuIds,
                                     @RequestParam("skuNums") List<Integer> skuNums){
        return productSkuService.deductInventories(skuIds, skuNums);
    }

    @PostMapping("updateSkuSaleNum")
    public void updateSkuSaleNum( @RequestParam("skuSaleDtoList") List<SkuSaleDto> skuSaleDtoList){
        productSkuService.updateSkuSaleNum(skuSaleDtoList);
    }


}