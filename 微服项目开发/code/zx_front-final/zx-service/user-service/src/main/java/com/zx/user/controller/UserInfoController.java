package com.zx.user.controller;

import com.zx.common.AuthThreadLocalUtils;
import com.zx.domain.dto.h5.UserLoginDto;
import com.zx.domain.dto.h5.UserRegisterDto;
import com.zx.domain.entity.user.UserInfo;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.common.ResultCodeEnum;
import com.zx.domain.vo.h5.UserInfoVo;
import com.zx.user.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "会员用户接口")
@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/user/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "会员注册")
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDto userRegisterDto){ //spring 自动将请求体中的 JSON 转换为 UserRegisterDto 对象。
        userInfoService.register(userRegisterDto);
        return  Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginDto userLoginDto){
        String token = userInfoService.login(userLoginDto);
        return  Result.build(token, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/auth/getCurrentUserInfo")
    public Result<UserInfoVo> getCurrentUserInfo(HttpServletRequest request) {
//        String token = request.getHeader("token");
//        UserInfoVo userInfoVo = userInfoService.getCurrentUserInfo(token) ;
//        return Result.build(userInfoVo , ResultCodeEnum.SUCCESS) ;

        UserInfo userInfoVo = AuthThreadLocalUtils.getThreadLocal();
        return Result.build(userInfoVo , ResultCodeEnum.SUCCESS) ;
    }


}
