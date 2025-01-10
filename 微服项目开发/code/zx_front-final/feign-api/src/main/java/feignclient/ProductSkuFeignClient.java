package feignclient;


import com.zx.domain.dto.product.SkuSaleDto;
import com.zx.domain.entity.product.ProductSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "product-service")
public interface ProductSkuFeignClient {
    @GetMapping("productSku/getProductById/{id}")
    public ProductSku getProductById(@PathVariable("id")Long id);

    @PostMapping("productSku/getSkuListBySkuIds")
    public List<ProductSku> getSkuListBySkuIds(@RequestParam("ids") List<Long> ids);

    @GetMapping("/productSku/deductInventory/{skuId}/{num}")
    public Boolean deductInventory(@PathVariable("skuId")Long skuId,
                                   @PathVariable("num")Integer num);

    @PostMapping("productSku/deductInventories")
    public Boolean deductInventories(@RequestParam("skuIds") List<Long> skuIds,
                                     @RequestParam("skuNums") List<Integer> skuNums);

    @PostMapping("productSku/updateSkuSaleNum")
    public void updateSkuSaleNum(@RequestParam("skuSaleDtoList") List<SkuSaleDto> skuSaleDtoList);

}
