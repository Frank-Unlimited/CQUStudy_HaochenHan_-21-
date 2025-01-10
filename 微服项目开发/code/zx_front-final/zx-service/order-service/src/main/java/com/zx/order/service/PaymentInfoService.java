package com.zx.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.domain.entity.pay.PaymentInfo;
import com.zx.domain.vo.common.Result;

import java.util.Map;

public interface PaymentInfoService extends IService<PaymentInfo> {
//    PaymentInfo savePaymentInfo(String token, String orderNo);
//
//    PaymentInfo findPaymentInfoByOrderNo(String orderNo);
//
//    void callback(Map<String, String> paramsMap);

    Result<String> submitAlipay(String orderNo);

    void callback(Map<String, String> paramsMap);
}
