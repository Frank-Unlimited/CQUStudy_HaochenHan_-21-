package com.zx.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.zx.domain.entity.user.UserAddress;
import com.zx.domain.entity.user.UserCollect;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.product.UserCollectVo;

public interface UserCollectService extends IService<UserCollect> {
    void collect(Long skuId);

    void cancelcollect(Long skuId);

    Boolean iscollect(Long skuId);

    Result<Page<UserCollectVo>> findUserCollectPage(Integer pageNum, Integer pageSize);
}
