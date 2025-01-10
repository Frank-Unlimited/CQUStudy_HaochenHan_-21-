package com.zx.product;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor; // 导入MyBatis-Plus的分页拦截器
import org.mybatis.spring.annotation.MapperScan; // 导入MapperScan注解，用于指定MyBatis的Mapper接口所在的包
import org.springframework.boot.SpringApplication; // 导入SpringApplication类，用于启动Spring Boot应用
import org.springframework.boot.autoconfigure.SpringBootApplication; // 导入Spring Boot的自动配置注解
import org.springframework.boot.web.servlet.ServletComponentScan; // 导入ServletComponentScan注解，用于扫描Servlet、Filter和Listener等组件
import org.springframework.context.annotation.Bean; // 导入Bean注解，用于声明Spring Bean
import org.springframework.context.annotation.ComponentScan; // 导入ComponentScan注解，用于扫描Spring组件

@SpringBootApplication // 标记这是一个Spring Boot应用程序，启用自动配置、组件扫描等功能
@MapperScan("com.zx.product.mapper") // 扫描指定包下的Mapper接口，自动生成MyBatis的代理对象
@ServletComponentScan("com.zx.common") // 扫描Servlet组件、Filter和Listener等，通常用于Web相关的配置
public class ProductServiceApplication {

    // 启动应用程序的main方法
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args); // 启动Spring Boot应用
    }

    // 配置MyBatis-Plus分页拦截器的@Bean方法
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        // 返回分页拦截器，MyBatis-Plus自动处理分页逻辑
        return new PaginationInnerInterceptor();
    }
}
