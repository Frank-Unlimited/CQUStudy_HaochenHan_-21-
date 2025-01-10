package com.zx.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.github.pagehelper.PageInfo;
import com.zx.common.AuthThreadLocalUtils;
import com.zx.domain.entity.product.ProductSku;
import com.zx.domain.entity.user.UserAddress;
import com.zx.domain.entity.user.UserCollect;
import com.zx.domain.entity.user.UserInfo;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.common.ResultCodeEnum;
import com.zx.domain.vo.product.UserCollectVo;
import com.zx.exception.CustomException;
import com.zx.user.feignclient.ProductSkuFeignClient;
import com.zx.user.mapper.UserAddressMapper;
import com.zx.user.mapper.UserCollectMapper;
import com.zx.user.service.UserAddressService;
import com.zx.user.service.UserCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.baomidou.mybatisplus.extension.toolkit.Db.getOne;
import static com.baomidou.mybatisplus.extension.toolkit.Db.save;

@Service
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements UserCollectService {

    @Autowired
    private ProductSkuFeignClient productSkuFeignClient;

    @Override
    public void collect(Long sukId) {
        UserInfo userInfo = AuthThreadLocalUtils.getThreadLocal();
        LambdaQueryWrapper<UserCollect> eq = Wrappers.lambdaQuery(UserCollect.class)
                .eq(UserCollect::getUserId, userInfo.getId())
                .eq(UserCollect::getSkuId, sukId);
        UserCollect one = getOne(eq, false);
        if (one!= null) {
            throw new CustomException(ResultCodeEnum.HAVE_COLLECEED);
        }
        UserCollect collect = new UserCollect();
        collect.setUserId(userInfo.getId());
        collect.setSkuId(sukId);
        save(collect);
    }

    @Override
    public void  cancelcollect(Long sukId){
        LambdaQueryWrapper<UserCollect> eq = Wrappers.lambdaQuery(UserCollect.class).eq(UserCollect::getUserId, AuthThreadLocalUtils.getThreadLocal().getId()).eq(UserCollect::getSkuId, sukId);
        remove(eq);
    }

    @Override
    public Boolean iscollect(Long sukId){
        LambdaQueryWrapper<UserCollect> eq = Wrappers.lambdaQuery(UserCollect.class).eq(UserCollect::getUserId, AuthThreadLocalUtils.getThreadLocal().getId()).eq(UserCollect::getSkuId, sukId);
        UserCollect one = getOne(eq, false);
        return one != null;
    }

    @Override
    public Result<Page<UserCollectVo>> findUserCollectPage(Integer pageNum, Integer pageSize) {
        //1: 从ThreadLocal中获取当前登陆的用户id
        Long userId = AuthThreadLocalUtils.getThreadLocal().getId();
        //2: 根据用户id 分页查询收藏的商品skuid
        Page<UserCollect> userCollectPage = lambdaQuery()
                .eq(UserCollect::getUserId, userId)
                .page(new Page<UserCollect>(pageNum, pageSize));
        List<UserCollect> records = userCollectPage.getRecords();
        if(records == null || records.isEmpty()){
            return Result.build(null, ResultCodeEnum.SUCCESS);
        }
        List<Long> skuIds =  records.stream().map(UserCollect::getSkuId).toList();
        //3: 用skuid列表作为条件查询出sku商品列表(远程调用)
        List<ProductSku> skuListBySkuIds = productSkuFeignClient.getSkuListBySkuIds(skuIds);

        List<UserCollectVo> resultList = skuListBySkuIds.stream().map(sku -> {
            UserCollectVo userCollectVo = new UserCollectVo();
            userCollectVo.setSkuName(sku.getSkuName());
            userCollectVo.setThumbImg(sku.getThumbImg());
            userCollectVo.setSalePrice(sku.getSalePrice());
            userCollectVo.setId(sku.getId());
            userCollectVo.setCreateTime(sku.getCreateTime());
            return userCollectVo;
        }).toList();

        // 5: user-service将List封装到result中返回
        PageInfo<UserCollectVo> pageInfo = new PageInfo<>(resultList);
        pageInfo.setTotal(userCollectPage.getTotal());
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPages((int)userCollectPage.getPages());
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

}
