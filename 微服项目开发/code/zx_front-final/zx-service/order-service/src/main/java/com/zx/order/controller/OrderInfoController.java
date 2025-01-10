package com.zx.order.controller;

import com.github.pagehelper.PageInfo;
import com.zx.domain.dto.h5.OrderInfoDto;
import com.zx.domain.entity.order.OrderInfo;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.common.ResultCodeEnum;
import com.zx.domain.vo.h5.TradeVo;
import com.zx.order.service.OrderInfoService;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.*;

@Tag(name = "和訂單相關的接口")
@RestController
@RequestMapping("/order/orderInfo")
public class OrderInfoController {

    @Resource
    private OrderInfoService orderInfoService;

    @Operation(summary = "通过购物车创建订单")
    @GetMapping("/auth/trade")
    public Result<TradeVo> trade(){
        TradeVo tradeVo = orderInfoService.trade();
        return Result.build(tradeVo, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "通过购买创建订单")
    @GetMapping("/auth/buy/{skuId}")
    public Result<TradeVo> buy(@PathVariable Long skuId){
        TradeVo tradeVo = orderInfoService.buy(skuId);
        return Result.build(tradeVo, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "提交订单")
    @GlobalTransactional
    @PostMapping("/auth/submitOrder")
//    public Result<Long> submitOrder(@RequestBody OrderInfoDto dto){
//        Long res = orderInfoService.submitOrder(dto);
//        return Result.build(res, ResultCodeEnum.SUCCESS);
//    }
    public Result<String > submitOrder(@RequestBody OrderInfoDto dto){
        Long res = orderInfoService.submitOrder(dto);
        return Result.build(res.toString(), ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "获取订单信息")
    @GetMapping("auth/{orderId}")
    public Result<OrderInfo> getOrderInfo(@PathVariable Long orderId) {
        OrderInfo orderInfo = orderInfoService.getOrderInfo(orderId);
        return Result.build(orderInfo, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "获取订单分页列表")
    @GetMapping("auth/{page}/{limit}")
    public Result<PageInfo<OrderInfo>> myOrderInfoList(@PathVariable("page") Integer page,
                                                       @PathVariable("limit") Integer limit,
                                                       Integer orderStatus){
        return Result.build(orderInfoService.myPageList(page,limit,orderStatus), ResultCodeEnum.SUCCESS);
    }
}
