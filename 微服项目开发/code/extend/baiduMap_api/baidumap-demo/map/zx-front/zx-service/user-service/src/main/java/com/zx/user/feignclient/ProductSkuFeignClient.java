package com.zx.user.feignclient;

import com.zx.domain.entity.product.ProductSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "product-service")
public interface ProductSkuFeignClient {
    @PostMapping("productSku/getSkuListBySkuIds")
    public List<ProductSku> getSkuListBySkuIds(@RequestParam("ids") List<Long> ids);
}