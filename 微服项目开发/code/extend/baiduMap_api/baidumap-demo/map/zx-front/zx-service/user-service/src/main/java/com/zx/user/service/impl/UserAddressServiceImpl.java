package com.zx.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.common.AuthThreadLocalUtils;
import com.zx.domain.entity.base.BaseEntity;
import com.zx.domain.entity.base.Region;
import com.zx.domain.entity.user.UserAddress;
import com.zx.domain.entity.user.UserInfo;
import com.zx.exception.CustomException;
import com.zx.user.mapper.UserAddressMapper;
import com.zx.user.service.IRegionService;
import com.zx.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Autowired
    IRegionService regionService;

    @Override
    public List<UserAddress> findListByCurrentUserId() {
        Long userId = AuthThreadLocalUtils.getThreadLocal().getId();
        LambdaQueryWrapper<UserAddress> eq = Wrappers.lambdaQuery(UserAddress.class).eq(UserAddress::getUserId, userId);
        return list(eq);
    }

    @Override
    public String saveAddress(UserAddress userAddress) {
        /*
         * 1、判断当前用户是否有默认地址
         * 2、如果没有默认地址，则新增地址，并且设为默认地址
         * 3、如果已有默认地址，则判断本次添加中是否勾选了默认地址,
         *  3.1 如果是，则修改原来的默认地址为0，并新增本次的默认地址
         *  3.2 如果否，则新增地址，不设为默认地址
         */
        //1、获取当前用户在数据库中是否有默认地址
        // 自定义一个方法,专门获取当前用户的默认地址
        UserInfo info = AuthThreadLocalUtils.getThreadLocal();
        UserAddress defaultAddress = getDefaultAddress(info.getId());
        // 手动设置一个userId和全地址
        Region province = regionService.getByCode(userAddress.getProvinceCode());
        Region city = regionService.getByCode(userAddress.getCityCode());
        Region district = regionService.getByCode(userAddress.getDistrictCode());
        StringBuilder sb = new StringBuilder();
        sb.append(province.getName()).append(city.getName()).append(district.getName()).append(userAddress.getAddress());
        userAddress.setFullAddress(sb.toString());
        userAddress.setUserId(info.getId());

        //2、如果没有默认地址，则新增地址，并且设为默认地址
        if (defaultAddress == null) {
            userAddress.setIsDefault(1);
            save(userAddress);
            return "新增成功";
        }
        //3、如果已有默认地址，则判断本次添加中是否勾选了默认地址,
        Integer isDefault = userAddress.getIsDefault();
        if(isDefault == 1){
            // 说明本次勾选了,把上一次的默认地址修改为0,并添加本次的地址为1
            defaultAddress.setIsDefault(0);
            updateById(defaultAddress);
        }
        save(userAddress);
        return "新增成功";
    }
    public UserAddress getDefaultAddress(Long userId) {
        LambdaQueryWrapper<UserAddress> eq = Wrappers.lambdaQuery(UserAddress.class)
                .eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getIsDefault, 1);
        UserAddress one = getOne(eq, false);
        return one;
    }

    @Override
    @Transactional
    public String updateAddressById(UserAddress userAddress){

        UserInfo userInfo = AuthThreadLocalUtils.getThreadLocal();
        System.out.println("userid: "+ userInfo.getId());
        System.out.println("useraddrid: "+ userAddress.getId());
        System.out.println("isdefault" + userAddress.getIsDefault());
        if(userAddress.getIsDefault()==0){
            // 查询当前修改地址外是否有默认收货地址
            List<UserAddress> list = lambdaQuery()
                    .eq(UserAddress::getUserId, userInfo.getId())
                    .eq(UserAddress::getIsDefault, 1)
                    //.eq(BaseEntity::getId, userAddress.getId())
                    .list();
            if(list==null || list.size()==0){
                throw new CustomException("必须有一个默认收货地址");
            }
            System.out.println("hihi3" + userAddress.getId());
            System.out.println("hihi4" + userAddress.getAddress());
            updateById(userAddress);
        }
        if(userAddress.getIsDefault() == 1){
            lambdaUpdate()
                    .set(UserAddress::getIsDefault, 0)
                    .eq(UserAddress::getUserId, userInfo.getId())
                    .ne(BaseEntity::getId, userAddress.getId())
                    .update();
            System.out.println("hihi" + userAddress.getId());
            updateById(userAddress);
            System.out.println("hihi2" + userAddress.getAddress());
        }
        return "更新成功";

    }

    @Override
    public String removeAddressById(String id){
        UserAddress userAddress = getById(id);
        if(userAddress.getIsDefault()==1){
            throw new CustomException("当前收货地址是默认地址");
        }
        removeById(id);
        return  "删除成功";
    }

}
