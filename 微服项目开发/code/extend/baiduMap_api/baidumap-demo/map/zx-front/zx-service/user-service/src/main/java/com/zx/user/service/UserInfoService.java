package com.zx.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.domain.dto.h5.UserLoginDto;
import com.zx.domain.dto.h5.UserRegisterDto;
import com.zx.domain.entity.user.UserInfo;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.h5.UserInfoVo;

public interface UserInfoService extends IService<UserInfo> {

    void sendSms(String phone);

    void register(UserRegisterDto userRegisterDto);

    String login(UserLoginDto userLoginDto);

    UserInfoVo getCurrentUserInfo(String token);
}
