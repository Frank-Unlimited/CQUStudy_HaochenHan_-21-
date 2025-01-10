package com.zx.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.domain.entity.product.Category;
import com.zx.product.mapper.CategoryMapper;
import com.zx.product.service.CategoryService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // 标记该类为一个Spring服务类，便于Spring容器管理
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

    @Cacheable(cacheNames = "categoryTree", key = "'categoryTree'")
    @Override
    public List<Category> findCategoryTree() {
	    /*
	    查询分类树
	     */
        List<Category> categoryListFromDB = getCategoryListFromDB();
        System.out.println("go DB");
        return categoryListFromDB;
    }

    @NotNull
    private List<Category> getCategoryListFromDB() {
        //1.查询出所有分类
        List<Category> categoryList = lambdaQuery().list();
        //2.从所有分类集合中  找出一级分类
        List<Category> oneLevelList = categoryList.stream().filter(category -> category.getParentId().longValue() == 0).collect(Collectors.toList());
        //3.遍历一份分类集合
        oneLevelList.forEach(oneCategory -> {
            //4.得到每个一级分类下的 二级分类集合
            List<Category> twoLevelList = categoryList.stream().filter(category -> category.getParentId().longValue() == oneCategory.getId().longValue()).collect(Collectors.toList());
            //5.遍历二级分类集合
            twoLevelList.forEach(twoCategory -> {
                //6.得到每个二级分类下的  三级分类集合
                List<Category> threeLevelList = categoryList.stream().filter(category -> category.getParentId().longValue() == twoCategory.getId().longValue()).collect(Collectors.toList());
                twoCategory.setChildren(threeLevelList);
            });
            oneCategory.setChildren(twoLevelList);
        });
        return oneLevelList;
    }

}

