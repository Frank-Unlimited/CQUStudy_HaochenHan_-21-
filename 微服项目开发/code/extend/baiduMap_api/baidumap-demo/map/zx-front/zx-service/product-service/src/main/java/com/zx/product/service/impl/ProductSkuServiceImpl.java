package com.zx.product.service.impl;

import com.github.pagehelper.PageInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.domain.dto.product.ProductDto;
import com.zx.domain.entity.base.BaseEntity;
import com.zx.domain.entity.product.Product;
import com.zx.domain.entity.product.ProductSku;
import com.zx.domain.vo.common.Result;
import com.zx.product.mapper.ProductMapper;
import com.zx.product.mapper.ProductSkuMapper;
import com.zx.product.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku> implements ProductSkuService{

//    @Autowired
//    private ProductSkuService productSkuService;
//
//    @Override
//    public Result<PageInfo<ProductSku>> findPage(Integer page, Integer limit, ProductDto){
//        /*
//         * 按条件分页查询
//         * 准备一个分页对象和查询条件，然后调用mapper的方法
//         * */
//        // 按条件查询product对应的id
//        List<Product> list = Produck
//    }

    @Override
    public List<ProductSku> getSkuListBySkuIds(List<Long> ids) {
        List<ProductSku> productSkuList = lambdaQuery().in(BaseEntity::getId, ids).list();
        return productSkuList;
    }
}
