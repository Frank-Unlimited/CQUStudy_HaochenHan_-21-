package com.itheima.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.service.AliPayService;
import com.itheima.util.AlipayConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AliPayServiceImpl implements AliPayService {

    private   AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.private_key, "json", AlipayConfig.input_charset, AlipayConfig.alipay_public_key, "RSA2");
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map createNative(String logId, String total_fee, String goodsName) {


        Map<String, Object> reqData = new HashMap<>();
        reqData.put("out_trade_no", logId);// 订单号
        reqData.put("subject", goodsName);// 标题
        reqData.put("total_amount", total_fee);// 金额 单位元
        reqData.put("timeout_express", "5m");//  允许付款的超时时间,5分钟

        try {
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();//创建API对应的request类
            //把订单信息转换为json对象的字符串
            request.setBizContent(mapper.writeValueAsString(reqData));
            //设置回调地址
            request.setNotifyUrl(AlipayConfig.notify_url);
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            System.out.println(response.getBody());

            System.out.println("===============");
            System.out.println(response.getCode());
            if("10000".equals(response.getCode())){
                Map map = new HashMap();
                map.put("code_url",response.getQrCode());
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /***
     *  支付宝支付成功,和已经关闭的订单返回的是同一个状态码? 支付宝提倡我们使用 查询方法查询订单状态
     *
     * @param out_trade_no
     * @return
     */
    @Override
    public Map queryPayStatus(String out_trade_no) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();//创建API对应的request类

        Map<String, Object> reqData = new HashMap<>();
        reqData.put("out_trade_no", out_trade_no);
        ObjectMapper mapper = new ObjectMapper();
        //把订单信息转换为json对象的字符串
        try {
            request.setBizContent(mapper.writeValueAsString(reqData));
            //设置回调地址
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            System.out.println(response.getBody());
            System.out.println(response.getCode());

            Map map= new HashMap();
            map.put("code",response.getCode());
            map.put("trade_no",response.getTradeNo());
            map.put("trade_status",response.getTradeStatus());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 关闭订单
     * @param logId
     * @return
     */
    @Override
    public Map close(String logId) {

        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();//创建API对应的request类

        Map<String, Object> reqData = new HashMap<>();
        reqData.put("out_trade_no", logId);
        ObjectMapper mapper = new ObjectMapper();
        //把订单信息转换为json对象的字符串
        try {
            request.setBizContent(mapper.writeValueAsString(reqData));
            //设置回调地址
            AlipayTradeCloseResponse response = alipayClient.execute(request);
            System.out.println(response.getBody());
            System.out.println(response.getCode());

            Map map= new HashMap();
            map.put("code",response.getCode());
            map.put("trade_no",response.getTradeNo());
            map.put("trade_status",response.getSubCode());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
