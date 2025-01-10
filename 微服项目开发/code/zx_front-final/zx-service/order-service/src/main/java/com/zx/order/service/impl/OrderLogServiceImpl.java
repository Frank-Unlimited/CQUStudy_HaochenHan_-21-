package com.zx.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.domain.entity.order.OrderItem;
import com.zx.domain.entity.order.OrderLog;
import com.zx.order.mapper.OrderItemMapper;
import com.zx.order.mapper.OrderLogMapper;
import com.zx.order.service.OrderItemService;
import com.zx.order.service.OrderLogService;
import org.springframework.stereotype.Service;

@Service
public class OrderLogServiceImpl extends ServiceImpl<OrderLogMapper, OrderLog> implements OrderLogService {
}
