package com.zx.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.common.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Autowired
    private RedisTemplate<String , String> redisTemplate;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        log.info("过滤器拦截到的请求path是:........................................... {}", path);
        //api接口，异步请求，校验用户必须登录
        String userInfo = this.getUserInfo(request);
        log.info("userinfo:===========: " + userInfo);
        if(null != userInfo) {
            // 设置请求头
            ServerHttpRequest mutableRequest = request.mutate()
                    .headers(httpHeaders -> {
                        // httpHeaders.add("userInfo", userInfo);
                        httpHeaders.add("userInfo", URLEncoder.encode(userInfo, StandardCharsets.UTF_8));
                    })
                    .build();
            return chain.filter(exchange.mutate().request(mutableRequest).build());
        }
        if(antPathMatcher.match("/api/**/auth/**", path)) {
            if(null == userInfo) {
                ServerHttpResponse response = exchange.getResponse();
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                return out(response, ResultCodeEnum.LOGIN_AUTH);
            }
        }
        return chain.filter(exchange);
    }

    public int getOrder() {
        return 0;
    }
    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        Result result = Result.build(null, resultCodeEnum);
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    private String getUserInfo(ServerHttpRequest request) {
        try {
            String token = "";
            List<String> tokenList = request.getHeaders().get("token");
            if(null  == tokenList ) {
                return null;
            }
            token = tokenList.get(0);
            String userInfoJson = redisTemplate.opsForValue().get("token:"+token);
            if (StringUtils.isEmpty(userInfoJson)){
                return null;
            }
            return userInfoJson;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
