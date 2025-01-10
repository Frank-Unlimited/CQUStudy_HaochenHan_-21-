package com.zx.product.mapper;

/*
    Mapper层实现与数据库的交互
 */

import com.zx.domain.entity.product.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper; // 提供基本的数据库操作方法
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    // 这里不需要编写任何方法，继承了 BaseMapper 接口后，已经包含了常用的数据库操作方法

}
