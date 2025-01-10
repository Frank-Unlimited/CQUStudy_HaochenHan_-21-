package com.zx.order.config.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

@Component
public class MyALiPayUtils {

    @Autowired
    private AliPayProperties aliPayProperties;

    public AlipayTradePagePayResponse pay(String orderNo, BigDecimal amount,String subject) throws AlipayApiException, IOException {
        System.out.println("getPrivateKey: "+ aliPayProperties.getPrivateKey());
        AlipayClient alipayClient = new DefaultAlipayClient(
                aliPayProperties.getServerUrl(),
                aliPayProperties.getAppId(),
                aliPayProperties.getPrivateKey(),
                "json",
                "utf-8",
                aliPayProperties.getPublicKey(),
                "RSA2");
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
//异步接收地址，仅支持http/https，公网可访问
        request.setNotifyUrl(aliPayProperties.getNotifyUrl());
//同步跳转地址，仅支持http/https
        request.setReturnUrl(aliPayProperties.getReturnUrl());
/******必传参数******/
        JSONObject bizContent = new JSONObject();
//商户订单号，商家自定义，保持唯一性
        bizContent.put("out_trade_no", orderNo);
//支付金额，最小值0.01元
        bizContent.put("total_amount", amount);
//订单标题，不可使用特殊符号
        bizContent.put("subject", subject);
//电脑网站支付场景固定传值FAST_INSTANT_TRADE_PAY
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");


        request.setBizContent(bizContent.toString());
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "POST");
// 如果需要返回GET请求，请使用
// AlipayTradePagePayResponse response = alipayClient.pageExecute(request,"GET");

        return response;
    }
}
