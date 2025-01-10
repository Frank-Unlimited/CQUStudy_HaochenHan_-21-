package com.zx.order.service;

import com.zx.domain.entity.h5.CartInfo;

import java.util.List;

public interface CartService {
    void addCart(Long skuId, Integer skuNum);

    List<CartInfo> getCartList();

    void deleteCartBySkuId(Long skuId);

    void isChecked(Long skuId, Integer isChecked);

    void allCheckCart(Integer isChecked);

    void clearCart();
}
