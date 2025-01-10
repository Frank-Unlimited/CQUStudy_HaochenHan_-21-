package com.zx.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zx.domain.entity.product.Product;
import com.zx.domain.entity.product.ProductDetails;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductDetailsMapper extends BaseMapper<ProductDetails> {

}
