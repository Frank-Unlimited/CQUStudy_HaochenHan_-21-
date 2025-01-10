package com.zx.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zx.domain.entity.user.UserInfo;
import com.zx.domain.vo.common.Result;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@WebFilter(urlPatterns = "/*")
@Component
public class MyFilter implements Filter {

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        String uri = request.getRequestURI();

        String userInfoJson = request.getHeader("userInfo");

        System.out.println("userinfo2:===========: " + userInfoJson);

        if (StringUtils.isEmpty(userInfoJson) && uri.contains("/auth/")){
            response.setHeader("Content-Type", "application/json;charset=UTF-8");

            Result result = Result.build(null, 208, "请先登录");
            byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
            response.getOutputStream().write(bits);
            return;
        }
        if(StringUtils.isNotEmpty(userInfoJson)){
            userInfoJson = URLDecoder.decode(userInfoJson, StandardCharsets.UTF_8) ;
            UserInfo userInfo = JSON.parseObject(userInfoJson, UserInfo.class);
            AuthThreadLocalUtils.setThreadLocal(userInfo);
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
