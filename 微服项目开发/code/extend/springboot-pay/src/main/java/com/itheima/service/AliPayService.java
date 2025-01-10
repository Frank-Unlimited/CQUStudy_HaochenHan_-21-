package com.itheima.service;

import java.util.Map;

public interface AliPayService {
    /**
     * 生成二维码
     * @param out_trade_no
     * @param total_fee
     * @return
     */

    Map createNative(String out_trade_no, String total_fee, String goodsName);

    /**
     * 查询支付订单状态
     * @param out_trade_no
     * @return
     */
    public Map queryPayStatus(String out_trade_no);

    /**
     * 关闭订单
     * @param logId
     * @return
     */
    Map close(String logId);
}
