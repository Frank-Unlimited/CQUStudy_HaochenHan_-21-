package com.zx.order.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.domain.dto.product.SkuSaleDto;
import com.zx.domain.entity.order.OrderInfo;
import com.zx.domain.entity.order.OrderItem;
import com.zx.domain.entity.order.OrderLog;
import com.zx.domain.entity.pay.PaymentInfo;
import com.zx.domain.vo.common.Result;
import com.zx.order.config.alipay.AliPayProperties;
import com.zx.order.config.alipay.MyALiPayUtils;
import feignclient.ProductSkuFeignClient;
import com.zx.order.mapper.PaymentInfoMapper;
import com.zx.order.service.OrderInfoService;
import com.zx.order.service.OrderItemService;
import com.zx.order.service.OrderLogService;
import com.zx.order.service.PaymentInfoService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private MyALiPayUtils myALiPayUtils;

    @Autowired
    private ProductSkuFeignClient productSkuFeignClient;

    @Autowired
    private OrderLogService orderLogService;

    @Autowired
    private AliPayProperties aliPayProperties;

    @Override
    public Result<String> submitAlipay(String orderNo) {

        // 查询订单信息
        OrderInfo info = orderInfoService.lambdaQuery()
                .eq(OrderInfo::getOrderNo, orderNo)
                .one();

        // 查询订单项列表
        List<OrderItem> orderItemList = orderItemService.lambdaQuery()
                .eq(OrderItem::getOrderId, info.getId())
                .list();

        info.setOrderItemList(orderItemList);

        // 构建支付内容描述
        StringJoiner sj = new StringJoiner("甄选商城的订单包含的商品信息:", ",",
                "支付总金额:" + info.getTotalAmount());
        info.getOrderItemList().forEach(item -> sj.add(item.getSkuName()));

        // 创建支付记录
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setPaymentStatus(0);
        paymentInfo.setOutTradeNo(orderNo);
        paymentInfo.setContent(sj.toString());
        paymentInfo.setUserId(info.getUserId());
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setAmount(info.getTotalAmount());
        paymentInfo.setPayType(1);
        paymentInfo.setUpdateTime(new Date());
        save(paymentInfo);


        try {
            // 调用支付宝API获取支付二维码
            AlipayTradePagePayResponse pay = myALiPayUtils.pay(orderNo,
                    info.getTotalAmount(), sj.toString());
            return Result.build(pay.getBody(), 200, "调用支付宝成功...");
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    @GlobalTransactional
    public void callback(Map<String, String> paramsMap) {
        // 验签名
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    paramsMap,
                    aliPayProperties.getPublicKey(),
                    "UTF-8",
                    "RSA2"
            );

            if (!signVerified) {
                throw new RuntimeException("验签失败");
            }
        } catch (AlipayApiException e) {
            throw new RuntimeException("验签失败", e);
        }

        System.out.println("----------------------paramsMap: " + JSON.toJSONString(paramsMap));

        // 获取支付宝返回的参数
        String outTradeNo = paramsMap.get("out_trade_no");
        String tradeNo = paramsMap.get("trade_no");
        String subject = paramsMap.get("subject");

        System.out.println("hihihi,");
        // 更新支付信息
        lambdaUpdate()
                .set(PaymentInfo::getOrderNo, tradeNo)
                .set(PaymentInfo::getPaymentStatus, 1)
                .set(PaymentInfo::getCallbackTime, new Date())
                .set(PaymentInfo::getCallbackContent, subject)
                .eq(PaymentInfo::getOutTradeNo, outTradeNo)
                .update();

        // 更新订单状态
        OrderInfo orderInfoDB = orderInfoService.lambdaQuery()
                .eq(OrderInfo::getOrderNo, outTradeNo)
                .one();

        if (orderInfoDB != null) {
            orderInfoDB.setOrderStatus(1);  // 订单已支付
            orderInfoDB.setPayType(1);      // 支付方式：支付宝
            orderInfoDB.setPaymentTime(new Date());
            orderInfoService.updateById(orderInfoDB);
        }

        // 记录订单日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfoDB.getId());
        orderLog.setProcessStatus(1); // 支付成功
        orderLog.setNote("支付宝支付成功");
        orderLogService.save(orderLog);

        // 更新商品销售数量
        List<OrderItem> orderItemList = orderItemService.lambdaQuery()
                .eq(OrderItem::getOrderId, orderInfoDB.getId())
                .list();

        List<SkuSaleDto> skuSaleDtoList = orderItemList.stream()
                .map(item -> {
                    SkuSaleDto skuSaleDto = new SkuSaleDto();
                    skuSaleDto.setSkuId(item.getSkuId());
                    skuSaleDto.setNum(item.getSkuNum());
                    return skuSaleDto;
                })
                .collect(Collectors.toList());

        // 通过 Feign 调用商品服务更新库存信息
        productSkuFeignClient.updateSkuSaleNum(skuSaleDtoList);
    }

}
