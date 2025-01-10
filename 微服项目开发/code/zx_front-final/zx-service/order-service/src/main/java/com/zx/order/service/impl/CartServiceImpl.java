package com.zx.order.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zx.common.AuthThreadLocalUtils;
import com.zx.domain.entity.h5.CartInfo;
import com.zx.domain.entity.product.ProductSku;
import com.zx.domain.vo.common.ResultCodeEnum;
import com.zx.exception.CustomException;
import com.zx.order.service.CartService;
import feignclient.ProductSkuFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private ProductSkuFeignClient productSkuFeignClient;

    @Override
    public void addCart(Long skuId, Integer skuNum) {
        Long id = AuthThreadLocalUtils.getThreadLocal().getId();
        Object object = redisTemplate.opsForHash().get("cart:" + id, "sku:" + skuId);

        if (object == null) {
            CartInfo cartInfo = new CartInfo();
            cartInfo.setUserId(id);
            cartInfo.setSkuId(skuId);
            ProductSku productSku = productSkuFeignClient.getProductById(skuId);

            //------校验商品是否已下架或库存不足-------//
            if(productSku.getStatus()!=null && productSku.getStatus() == -1){
                throw new CustomException(ResultCodeEnum.PRODUCT_DELETED);
            }
            if(productSku.getStockNum() <= 0){
                throw new CustomException(ResultCodeEnum.STOCK_LESS);
            }
            //----------end-----------//

            cartInfo.setCartPrice(productSku.getSalePrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setImgUrl(productSku.getThumbImg());
            cartInfo.setSkuName(productSku.getSkuName());
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());

            // 手动序列化为 JSON 字符串
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String cartInfoJson = objectMapper.writeValueAsString(cartInfo);
                redisTemplate.opsForHash().put("cart:" + id, "sku:" + skuId, cartInfoJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        // 如果本身存在，则只是更新数量
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String cartInfoJson = (String) object;
            CartInfo cartinfo = objectMapper.readValue(cartInfoJson, CartInfo.class);  // 手动反序列化
            cartinfo.setSkuNum(cartinfo.getSkuNum() + skuNum);
            cartinfo.setUpdateTime(new Date());

            redisTemplate.opsForHash().put("cart:" + id, "sku:" + skuId, objectMapper.writeValueAsString(cartinfo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CartInfo> getCartList() {
        Long id = AuthThreadLocalUtils.getThreadLocal().getId();
        ObjectMapper objectMapper = new ObjectMapper();

        // 获取 Redis 中的数据
        List<Object> objects = redisTemplate.opsForHash().values("cart:" + id);

        // 反序列化为 List<CartInfo>
        List<CartInfo> cartList = objects.stream()
                .map(object -> {
                    try {
                        String json = (String) object;
                        return objectMapper.readValue(json, CartInfo.class);
                    } catch (Exception e) {
                        throw new RuntimeException("反序列化失败", e);
                    }
                })
                .collect(Collectors.toList());


        System.out.println("cartlist: " + cartList);

        // 排序
        cartList.sort((info1, info2) -> (int) (info2.getUpdateTime().getTime() - info1.getUpdateTime().getTime()));
        return cartList;
    }

    @Override
    public void deleteCartBySkuId(Long skuId){
        Long id = AuthThreadLocalUtils.getThreadLocal().getId();
        redisTemplate.opsForHash().delete("cart:" + id, "sku:" + skuId);
    }

    @Override
    public void isChecked(Long skuId, Integer isChecked){

        Long id = AuthThreadLocalUtils.getThreadLocal().getId();

        // 从 Redis 中获取值，返回的是 Object 类型（实际是 JSON 字符串）
        Object redisValue = redisTemplate.opsForHash().get("cart:" + id, "sku:" + skuId);
        if (redisValue == null) return;

        // 使用 Jackson 将 JSON 字符串反序列化为 CartInfo 对象
        ObjectMapper objectMapper = new ObjectMapper();
        CartInfo info;
        try {
            info = objectMapper.readValue(redisValue.toString(), CartInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 反序列化失败", e);
        }

        info.setIsChecked(isChecked);

        try {
            // 将 CartInfo 序列化为 JSON 字符串并存回 Redis
            redisTemplate.opsForHash().put("cart:" + id, "sku:" + skuId, new ObjectMapper().writeValueAsString(info));
        } catch (JsonProcessingException e) {
            // 异常处理逻辑，可以记录日志或抛出自定义异常
            throw new RuntimeException("JSON 序列化失败", e);
        }

    }

    @Override
    public void allCheckCart(Integer isChecked) {
        Long id = AuthThreadLocalUtils.getThreadLocal().getId();
        ObjectMapper objectMapper = new ObjectMapper();

        // 获取 Redis 中的数据
        List<Object> objects = redisTemplate.opsForHash().values("cart:" + id);

        // 反序列化为 List<CartInfo>
        List<CartInfo> cartList = objects.stream()
                .map(object -> {
                    try {
                        String json = (String) object;
                        return objectMapper.readValue(json, CartInfo.class);
                    } catch (Exception e) {
                        throw new RuntimeException("反序列化失败", e);
                    }
                })
                .collect(Collectors.toList());


        for (CartInfo info : cartList) {
            info.setIsChecked(isChecked);

            try {
                // 将 CartInfo 序列化为 JSON 字符串并存回 Redis
                redisTemplate.opsForHash().put("cart:" + id, "sku:" + id, new ObjectMapper().writeValueAsString(info));
            } catch (JsonProcessingException e) {
                // 异常处理逻辑，可以记录日志或抛出自定义异常
                throw new RuntimeException("JSON 序列化失败", e);
            }

        }
    }

    @Override
    public void clearCart(){
        Long id = AuthThreadLocalUtils.getThreadLocal().getId();
        redisTemplate.delete("cart:"+id);
    }


}
