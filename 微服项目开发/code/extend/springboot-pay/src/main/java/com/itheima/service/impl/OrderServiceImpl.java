package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import com.itheima.domain.Order;
import com.itheima.domain.PayLog;
import com.itheima.mapper.OrderMapper;
import com.itheima.mapper.PayLogMapper;
import com.itheima.service.OrderService;
import com.itheima.service.WeixinPayService;
import com.itheima.util.PayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private PayLogMapper payLogMapper;
    @Autowired
    private WeixinPayService weixinPayService;

    @Override
    public List<Order> findAll(){
        return orderMapper.selectList(null);
    }

    @Override
    public void add(Order order) {
        order.setCreatetime(new Date());
        order.setUpdatetime(new Date());
        order.setStatus("0");//0  待支付,1 已支付 2,订单超时关闭,3,客户主动取消关闭
        orderMapper.insert(order);
    }
    // 将订单状态更新为支付成功
    @Override
    public void updateOrderStatus(String orderId, String transaction_id, String logId) {

        Order order = new Order();
        order.setOrderid(orderId);
        order.setUpdatetime(new Date());
        order.setStatus("1");// 支付成功
        order.setClosetime(new Date());// 支付成功
        order.setPaymenttime(new Date());
        //orderMapper.updateOrder(order);
        orderMapper.updateById(order);

        PayLog log= new PayLog();
        log.setLogid(logId);
        log.setTransactionid(transaction_id);
        log.setTradestate("1");
        log.setPaytime(new Date());
        //payLogMapper.update(log);
        payLogMapper.updateById(log);
    }
    // 微信支付成功回调函数
    @Override
    public boolean WXSuccessCallback(String xmlRet) {
        try {
            // 校验签名是否正确
            boolean flag = WXPayUtil.isSignatureValid(xmlRet, PayConfig.partnerkey);
            // 如果成功,
            if(flag){
                // 转换
                Map<String, String> retmap = WXPayUtil.xmlToMap(xmlRet);
                String result_code = retmap.get("result_code");
                if("SUCCESS".equals(result_code)){
                    String transaction_id = retmap.get("transaction_id");
                    String out_trade_no = retmap.get("out_trade_no");
                    //PayLog log = payLogMapper.findById(out_trade_no);
                    PayLog log = payLogMapper.selectById(out_trade_no);
                    // 更新数据库保存成功
                    updateOrderStatus(log.getOrderid(),transaction_id,out_trade_no);
                }else {
                    // 如果不是success  应该是微信除了问题,以防万一
                    return  false;
                }
            }
            return  flag;
        } catch (Exception e) {
            return false;
        }

    }
    @Override
    public Order findByid(String orderid){
        return orderMapper.selectById(orderid);
    }


}
