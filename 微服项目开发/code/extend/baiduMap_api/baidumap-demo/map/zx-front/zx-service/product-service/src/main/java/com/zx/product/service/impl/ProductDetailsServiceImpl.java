package com.zx.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.domain.entity.product.Product;
import com.zx.domain.entity.product.ProductDetails;
import com.zx.product.mapper.ProductDetailsMapper;
import com.zx.product.mapper.ProductMapper;
import com.zx.product.service.ProductDetailsService;
import com.zx.product.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailsServiceImpl extends ServiceImpl<ProductDetailsMapper, ProductDetails> implements ProductDetailsService {
}
