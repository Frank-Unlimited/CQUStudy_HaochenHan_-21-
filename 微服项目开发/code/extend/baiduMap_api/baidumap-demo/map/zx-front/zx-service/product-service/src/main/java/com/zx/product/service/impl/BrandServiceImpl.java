package com.zx.product.service.impl;

import com.zx.domain.entity.product.Brand;
import com.zx.product.mapper.BrandMapper;
import com.zx.product.service.BrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
}
