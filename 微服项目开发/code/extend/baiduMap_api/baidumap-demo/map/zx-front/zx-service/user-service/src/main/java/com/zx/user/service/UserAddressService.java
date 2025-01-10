package com.zx.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.domain.entity.user.UserAddress;

import java.util.List;

public interface UserAddressService extends IService<UserAddress> {

    List<UserAddress> findListByCurrentUserId();

    String saveAddress(UserAddress userAddress);

    String updateAddressById(UserAddress userAddress);

    String removeAddressById(String id);
}
