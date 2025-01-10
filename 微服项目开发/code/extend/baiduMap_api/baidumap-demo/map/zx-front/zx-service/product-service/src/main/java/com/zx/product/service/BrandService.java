package com.zx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.domain.entity.product.Brand;
import com.zx.domain.entity.product.Category;
import com.zx.product.mapper.BrandMapper;
import org.springframework.stereotype.Service;

@Service
public interface BrandService extends IService<Brand> {
}
