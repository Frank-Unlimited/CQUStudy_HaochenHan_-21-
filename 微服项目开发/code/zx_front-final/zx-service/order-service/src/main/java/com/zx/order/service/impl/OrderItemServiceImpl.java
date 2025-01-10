package com.zx.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.domain.entity.order.OrderItem;
import com.zx.domain.entity.product.ProductDetails;
import com.zx.order.mapper.OrderItemMapper;
import com.zx.order.service.CartService;
import com.zx.order.service.OrderItemService;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements  OrderItemService {
}
