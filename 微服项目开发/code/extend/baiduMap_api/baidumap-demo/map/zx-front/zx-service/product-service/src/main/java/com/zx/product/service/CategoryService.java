package com.zx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import com.zx.domain.entity.product.Category; // 分类实体类

import java.util.List;


public interface CategoryService extends IService<Category> {
    List<Category> findCategoryTree();
}
