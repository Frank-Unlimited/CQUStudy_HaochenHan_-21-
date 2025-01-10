package com.zx.product.service;

import com.alipay.api.domain.PageInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.domain.dto.product.ProductDto;
import com.zx.domain.entity.product.ProductSku;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.h5.ProductItemVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductSkuService extends IService<ProductSku> {
//    Result index();
//
//    Result<PageInfo> findPage(Integer page, Integer limit, ProductDto dto);
//
//    Result<ProductItemVo> getItem(Long skuId);
//
//    List<ProductSku> getProductSkuByProductId(Long productId);

    List<ProductSku> getSkuListBySkuIds(List<Long> ids);


}