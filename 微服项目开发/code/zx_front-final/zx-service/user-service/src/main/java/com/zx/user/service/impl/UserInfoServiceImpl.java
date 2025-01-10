package com.zx.user.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zx.domain.vo.h5.UserInfoVo;
import com.zx.exception.CustomException;
import com.zx.utils.MyJwtUtils;
import com.alipay.api.domain.UserInfoDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.domain.dto.h5.UserLoginDto;
import com.zx.domain.dto.h5.UserRegisterDto;
import com.zx.domain.entity.user.UserInfo;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.common.ResultCodeEnum;
import com.zx.user.mapper.UserInfoMapper;
import com.zx.user.service.UserInfoService;

import com.zx.utils.MySendMessageUtils;
import com.zx.utils.RandomStringUtils;
import com.zx.utils.SendMailUtils;
import io.micrometer.observation.annotation.Observed;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private MyJwtUtils myJwUtils;


    @Override
    public void sendSms(String phone) {
        // String code = String.valueOf((int)((Math.random()*9+1)*1000));
        RandomStringUtils randomStringUtils = new RandomStringUtils(); // 创建实例
        String code = randomStringUtils.generateRandomNumberString(4);

        redisTemplate.opsForValue().set("phone:"+phone, code,24, TimeUnit.HOURS);
        System.out.println(redisTemplate.opsForValue().get("phone:" + phone)); // debug
         try {
             if (isEmail(phone)) {
                 // 如果是邮箱
                 SendMailUtils.sendEmail(phone, "验证码", code);
             } else if (isPhoneNumber(phone)) {
                 // 如果是电话号码
                 MySendMessageUtils.sendMessage(code, phone);
             } else {
                 throw new IllegalArgumentException("无效的 phone 格式: 既不是邮箱也不是电话号码");
             }

         } catch (Exception e) {
             throw new RuntimeException(e);
         }
    }

    // 工具方法：检测是否是邮箱
    private static boolean isEmail(String input) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        return input != null && input.matches(emailRegex);
    }

    // 工具方法：检测是否是电话号码
    private static boolean isPhoneNumber(String input) {
        String phoneRegex = "^1[3-9]\\d{9}$"; // 简单校验中国大陆手机号
        return input != null && input.matches(phoneRegex);
    }

    @Override
    public void register(UserRegisterDto userRegisterDto){
        // 校验信息是否有缺失
        if(userRegisterDto.getUsername() == null
        || userRegisterDto.getPassword() == null
        || userRegisterDto.getCode() == null
        || userRegisterDto.getNickName() == null){
            throw new CustomException(ResultCodeEnum.PARAM_ERROR);
        }
        // 校验是否手机号已注册
        String username = userRegisterDto.getUsername();
        UserInfo existingUser = baseMapper.selectOne(new QueryWrapper<UserInfo>().eq("username", username));
        if (existingUser != null) {
            throw new CustomException(ResultCodeEnum.RE_REGISTERED);
        }
        // 验证
        String code = userRegisterDto.getCode();
        String redisCode = redisTemplate.opsForValue().get("phone:" + userRegisterDto.getUsername());
//        System.out.println("now phone: " +  userRegisterDto.getUsername());
//        String correct_code = redisTemplate.opsForValue().get("phone:" + "18996183106");
//        System.out.println("correct code: " +  correct_code);
//        System.out.println("now code: " +  redisCode);
        if(StringUtils.isBlank(redisCode)){
            throw new CustomException(ResultCodeEnum.TIMEOUT_VALIDATION);
        }
        if (!code.equals(redisCode)){
            throw new CustomException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        // 加密
        String password = userRegisterDto.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        userRegisterDto.setPassword(password);

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userRegisterDto, userInfo);

        // 补充一些数据
        userInfo.setAvatar("https://pics7.baidu.com/feed/9d82d158ccbf6c8109428c4449b4263832fa40b9.jpeg@f_auto?token=6659f4c24b8442587ef01f8793f2caa5");
        userInfo.setPhone(userInfo.getUsername());
        baseMapper.insert(userInfo);

    }

    @Override
    public String login(UserLoginDto dto){

        LambdaQueryWrapper<UserInfo> eq = Wrappers.lambdaQuery(UserInfo.class)
                .eq(UserInfo::getPhone, dto.getUsername());
        UserInfo userInfo = this.getOne(eq);

        if(userInfo == null) {
            // return Result.build("用户名不存在", ResultCodeEnum.LOGIN_ERROR);
            throw new CustomException(ResultCodeEnum.USER_NON_EXISTS);
        }

        String password = dto.getPassword(); // 明文密码
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes()); // 再次加密对应数据库
        if (!userInfo.getPassword().equals(passwordMd5)){
            //return Result.build("密码错误", ResultCodeEnum.LOGIN_ERROR);
            throw new CustomException(ResultCodeEnum.LOGIN_ERROR);
        }

        String s = myJwUtils.generateToken(userInfo.getId() + "");
        redisTemplate.opsForValue().set("token:" + s, JSON.toJSONString(userInfo), 10,
                TimeUnit.HOURS);

        System.out.println("token:" + s);
        return s;

    }


    @Override
    public UserInfoVo getCurrentUserInfo(String token) {
        //1.从redis获取用户信息
        String userInfoJsonStr = redisTemplate.opsForValue().get("token:" + token);
        if(StringUtils.isBlank(userInfoJsonStr)){
            throw new CustomException(ResultCodeEnum.LOGIN_AUTH);
        }
        UserInfo userInfo = JSON.parseObject(userInfoJsonStr, UserInfo.class);
        //2.封装vo返回
        UserInfoVo vo = new UserInfoVo();
        // BeanUtils.copyProperties(userInfo, vo);
        vo.setNickName(userInfo.getNickName());
        vo.setAvatar(userInfo.getAvatar());
        return vo;
    }

}
