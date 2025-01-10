package com.zx.user.controller;


import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.common.ResultCodeEnum;
import com.zx.user.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户短信相关接口")
@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class SmsController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping(value = "/sms/sendCode/{phone}")
    @Operation(summary = "发送短信验证码")
    public Result sendValidateCode(@PathVariable String phone){
        userInfoService.sendSms(phone);
        return  Result.build(null, ResultCodeEnum.SUCCESS);
    }

}
