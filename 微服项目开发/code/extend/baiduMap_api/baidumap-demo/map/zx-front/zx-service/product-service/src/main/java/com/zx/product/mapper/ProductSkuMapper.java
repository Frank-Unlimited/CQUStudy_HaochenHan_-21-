package com.zx.product.mapper;
/*
    Mapper层实现与数据库的交互
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zx.domain.entity.product.ProductSku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSku> {
    // 这里不需要编写任何方法，继承了 BaseMapper 接口后，已经包含了常用的数据库操作方法

}
