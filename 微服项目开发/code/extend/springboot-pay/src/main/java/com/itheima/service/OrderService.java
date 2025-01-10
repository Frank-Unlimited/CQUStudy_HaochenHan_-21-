package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.Order;

import java.util.List;

public interface OrderService extends IService<Order> {
    public List<Order> findAll();
    public void add(Order order);

    void updateOrderStatus(String orderId, String transaction_id, String logId);
    public boolean WXSuccessCallback(String xmlRet);

    Order findByid(String orderid);
}
