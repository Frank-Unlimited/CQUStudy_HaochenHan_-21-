package com.zx.order.config.alipay;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(AliPayProperties.class)
@Configuration
public class MyAliPayConfig {
}
