package com.zx.order.controller;

import com.zx.domain.entity.h5.CartInfo;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.common.ResultCodeEnum;
import com.zx.order.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "添加购物车")
    @GetMapping("/auth/addToCart/{skuId}/{skuNum}")
    public Result<String> addToCart(@PathVariable Long skuId, @PathVariable Integer skuNum){
        System.out.println("我执行了");
        cartService.addCart(skuId, skuNum);
        return Result.build("ok", ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "查询购物车")
    @GetMapping("auth/cartList")
    public Result<List<CartInfo>> cartList() {
        List<CartInfo> cartInfoList = cartService.getCartList();
        return Result.build(cartInfoList, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "删除购物车")
    @DeleteMapping("auth/deleteCart/{skuId}")
    public Result<String> deleteCartBySkuId(@PathVariable("skuId") Long skuId){
        cartService.deleteCartBySkuId(skuId);
        return Result.build("ok", ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "判断购物车商品是否选中")
    @GetMapping("auth/checkCart/{skuId}/{isChecked}")
    public Result<String> isChecked(@PathVariable("skuId") Long skuId, @PathVariable("isChecked") Integer isChecked){
        cartService.isChecked(skuId, isChecked);
        return Result.build("ok", ResultCodeEnum.SUCCESS);
    }

    @Operation(summary="更新购物车商品全部选中状态")
    @GetMapping("auth/allCheckCart/{isChecked}")
    public Result<String> allCheckCart( @PathVariable("isChecked") Integer isChecked){
        cartService.allCheckCart(isChecked);
        return Result.build("ok", ResultCodeEnum.SUCCESS);
    }

    @Operation(summary="清空购物车")
    @GetMapping("/auth/clearCart")
    public Result<String> clearCart(){
        cartService.clearCart();
        return Result.build("ok", ResultCodeEnum.SUCCESS);
    }

}
