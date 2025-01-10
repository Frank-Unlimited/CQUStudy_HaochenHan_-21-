package com.zx.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 在hutool工具的基础上进行了二次封装
 */
@Component
public class MyJwtUtils {
    // 自定义密钥

    @Value("${jwt.secretKey:jyzx_zhangXueQuan}")
    private  String SECRET_KEY ;
    @Value("${jwt.expirationMinutes:36000000}")
    private  int expirationMinutes ;

    public  String generateToken(String content) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("content", content);
        payload.put("exp", DateUtil.offsetMinute(DateUtil.date(), expirationMinutes).getTime());

        return JWTUtil.createToken(payload, SECRET_KEY.getBytes());
    }
    public  String parseToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }

        try {
            JWT jwt = JWTUtil.parseToken(token);
            if (!jwt.setKey(SECRET_KEY.getBytes()).verify()) {
                return null; // 验证失败
            }

            long exp = Long.valueOf(jwt.getPayload("exp").toString()) ;
            if (exp < System.currentTimeMillis()) {
                return null; // Token已过期
            }

            return jwt.getPayload("content").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 解析失败
        }
    }


    public static void main(String[] args) {
        MyJwtUtils myJwtUtils = new MyJwtUtils();
        myJwtUtils.SECRET_KEY="abc";
        myJwtUtils.expirationMinutes=36000;
        String token = myJwtUtils.generateToken("123");
        System.out.println(token);
        String s = myJwtUtils.parseToken(token);
        System.out.println(s);
    }
}
