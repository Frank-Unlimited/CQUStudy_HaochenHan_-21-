package com.zx.user.controller;


import com.zx.domain.entity.user.UserAddress;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.common.ResultCodeEnum;
import com.zx.user.service.UserAddressService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户地址表(UserAddress)表控制层
 */
@RestController
@RequestMapping("/user/userAddress")
public class UserAddressController {

    @Resource
    private UserAddressService userAddressService;

    @GetMapping("/auth/findUserAddressList")
    public Result<List<UserAddress>> findUserAddressList() {
        List<UserAddress> list = userAddressService.findListByCurrentUserId();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/auth/save")
    public Result<String> saveAddress(@RequestBody UserAddress userAddress) {
        String res = userAddressService.saveAddress(userAddress);
        return Result.build(res, ResultCodeEnum.SUCCESS);
    }

    @PutMapping("auth/updateById")
    public Result<String> updateAddressById(@RequestBody UserAddress userAddress) {
        String res = userAddressService.updateAddressById(userAddress);
        return Result.build(res, ResultCodeEnum.SUCCESS);
    }

    @DeleteMapping("auth/removeById/{id}")
    public Result<String> removeById(@PathVariable String id){
        String res = userAddressService.removeAddressById(id);
        return Result.build(res, ResultCodeEnum.SUCCESS);
    }

}