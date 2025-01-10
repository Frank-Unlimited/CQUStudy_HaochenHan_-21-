package com.zx.product.controller;
/*
    和前端数据请求交互
 */

import com.zx.domain.vo.common.Result; // 导入 Result 类，用于统一的返回结果封装
import com.zx.domain.vo.common.ResultCodeEnum; // 导入 ResultCodeEnum 枚举类，包含统一的状态码
import com.zx.domain.vo.h5.IndexVo; // 导入 IndexVo 类，封装首页数据
import com.zx.product.service.ProductService; // 导入 ProductService 接口，用于调用业务逻辑
import io.swagger.v3.oas.annotations.Operation; // 导入 Swagger 注解，描述 API 操作
import io.swagger.v3.oas.annotations.tags.Tag; // 导入 Swagger 注解，用于标记控制器类的描述
import org.springframework.beans.factory.annotation.Autowired; // 导入 Autowired 注解，用于自动注入依赖
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping; // 导入 GetMapping 注解，处理 GET 请求
import org.springframework.web.bind.annotation.RequestMapping; // 导入 RequestMapping 注解，用于设置路径
import org.springframework.web.bind.annotation.RestController; // 导入 RestController 注解，声明该类是一个 RESTful 控制器

// 解决跨域问题
@EnableCaching
//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/product")
@Tag(name = "首页")
public class IndexController {

    // 自动注入 ProductService，ProductService 实现了业务逻辑
    @Autowired
    private ProductService productService;

    /**
     * 处理 "/product/index" 路径的 GET 请求
     * 该接口用于获取首页数据（例如：一级分类、热销商品等）
     *
     * @return 包装了首页数据的 Result 对象
     */
    @Operation(summary = "首页接口")
    @GetMapping("/index")
    public Result<IndexVo> findIndexData() {
        // 调用 ProductService 的业务方法获取首页数据
        IndexVo vo = productService.findIndexData();

        // 返回封装好的 Result 对象，包含查询结果和统一的响应状态码
        return Result.build(vo, ResultCodeEnum.SUCCESS);
    }
}
