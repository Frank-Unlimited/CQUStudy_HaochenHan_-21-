package com.zx.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zx.domain.entity.user.UserCollect;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.common.ResultCodeEnum;
import com.zx.domain.vo.product.UserCollectVo;
import com.zx.user.service.UserCollectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户收藏接口")
@RestController
@RequestMapping("/user/userInfo")
public class UserCollectController {

    @Resource
    private UserCollectService userCollectService;

    @GetMapping("/auth/collect/{sukId}")
    public Result<String> collect(@PathVariable("sukId") Long sukId){
        userCollectService.collect(sukId);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @GetMapping("auth/cancelCollect/{sukId}")
    public Result<String> cancelCollect(@PathVariable("sukId") Long sukId) {
        userCollectService.cancelcollect(sukId);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @GetMapping("isCollect/{sukId}")
    public Result<Boolean> isCollect(@PathVariable("sukId") Long sukId) {

        return Result.build(userCollectService.iscollect(sukId), ResultCodeEnum.SUCCESS);
    }

    @GetMapping("auth/findUserCollectPage/{pageNum}/{pageSize}")
    public Result<Page<UserCollectVo>> findUserCollectPage(@PathVariable("pageNum") Integer pageNum,
                                                           @PathVariable("pageSize") Integer pageSize) {
        return userCollectService.findUserCollectPage(pageNum, pageSize);
    }
}
