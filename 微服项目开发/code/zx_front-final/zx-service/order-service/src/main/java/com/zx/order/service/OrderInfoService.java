package com.zx.order.service;

import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zx.domain.dto.h5.OrderInfoDto;
import com.zx.domain.entity.order.OrderInfo;
import com.zx.domain.vo.common.Result;
import com.zx.domain.vo.h5.TradeVo;

public interface OrderInfoService extends IService<OrderInfo> {
    TradeVo trade();

    TradeVo buy(Long skuId);

    Long submitOrder(OrderInfoDto dto);

    OrderInfo getOrderInfo(Long orderId);

    PageInfo<OrderInfo> myPageList(Integer page, Integer limit, Integer orderStatus);

}
