package com.zx.product.service;

import com.github.pagehelper.PageInfo;
import com.zx.domain.dto.h5.ProductSkuDto;
import com.zx.domain.dto.product.ProductDto;
import com.zx.domain.entity.product.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.domain.entity.product.ProductSku;
import com.zx.domain.vo.h5.IndexVo;
import com.zx.domain.vo.h5.ProductItemVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService extends IService<Product> {
    IndexVo findIndexData();

    PageInfo<ProductSku> findByPage(Integer pageNum, Integer pageSize, ProductSkuDto dto);

    ProductItemVo item(Long skuId);


}