package com.itheima.controller;

import com.itheima.domain.Order;
import com.itheima.domain.PayLog;
import com.itheima.domain.Result;
import com.itheima.service.AliPayService;
import com.itheima.service.OrderService;
import com.itheima.service.PayLogService;
import com.itheima.service.WeixinPayService;
import com.itheima.util.PayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private PayLogService payLogService;
    @Autowired
    private WeixinPayService weixinPayService;
    @Autowired
    private AliPayService aliPayService;

    @RequestMapping("/findAll")
    public List<Order> findAll() {
        System.out.println(orderService.findAll().get(0).getCreatetime());
        return orderService.findAll();
    }

    @RequestMapping("/add")
    public Result add(Order order) {
        // 不全订单信息

        // 1 补全代码,生成订单号
        String orderId = PayUtil.getId("01");//传入业务号返回业务id
        String logId = PayUtil.getId("02"); // 获取日志表号码
        order.setOrderid(orderId);

        orderService.add(order);
        // 2 保存支付日志信息
        // 支付金额单位分
        String money = PayUtil.getMoney(order.getPayment() + "");
        PayLog log = new PayLog();
        log.setLogid(logId);
        log.setCreatetime(new Date());
        log.setOrderid(orderId);
        log.setOuttradeno(logId);// 支付订单号
        log.setPaytype(order.getPaymenttype());// 微信支付
        log.setUserid(order.getUserid());
        log.setTotalfee(Integer.parseInt(money));
        log.setTradestate("0");// 未支付
        payLogService.add(log);

        // 2)如果是微信支付,调用微信支付生成二维码
        if ("1".equals(order.getPaymenttype())) {
            // 3) 调用微信 获取支付信息,,
            //此处的order.getOrderid() 在调用完  orderService.add(order); 后 因为是地址值引用所以能拿到生成的id;
            Map map = weixinPayService.createNative(logId, money);
            map.put("paymenttype", "1");
            map.put("orderId", orderId);
            map.put("logId", logId);
            // 4) 生成网页二维码基本信息,并返回
            Result result = new Result(true, "订单生成成功", map);
            return result;
        } else if ("2".equals(order.getPaymenttype())) {

            Map map = aliPayService.createNative(logId, order.getPayment() + "", order.getGoodsid());
            if (map == null) {
                return new Result(false, "订单生成失败", null);
            }
            map.put("paymenttype", "2");
            map.put("orderId", orderId);
            map.put("logId", logId);
            map.put("total_fee", order.getPayment());
            // 4) 生成网页二维码基本信息,并返回
            Result result = new Result(true, "订单生成成功", map);
            return result;
        } else {
            // 如果是其他支付
            Map map = new HashMap();
            map.put("paymenttype", "2");
            map.put("orderId", orderId);
            map.put("logId", logId);
            Result result = new Result(true, "订单生成成功", map);
            return result;
        }
    }

    /**
     * 微信查询订单状态
     * @param logId
     * @param orderId
     * @return
     */
    // 查询订单状态
    @RequestMapping("/findPayStatus")
    public Result findPayStatus(String logId, String orderId) {
        Result result = null;
        int x = 0;
        while (true) {

            Map<String, String> map = weixinPayService.queryPayStatus(logId);//调用查询
            if (map == null) {
                result = new Result(false, "支付发生错误", "2");
                break;
            }
            if (map.get("trade_state").equals("SUCCESS")) {//支付成功
                result = new Result(true, "支付成功", "1");
                orderService.updateOrderStatus(orderId, map.get("transaction_id"), logId);//修改订单状态
                break;
            }
            // 循环 100次 每次 3秒 共5分钟
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            x++;
            if (x >= 10) {
                result = new Result(false, "二维码超时", "3");
                break;
            }

        }
        return result;
    }

    /**
     * 微信关闭订单
     * @param logId
     * @param orderId
     * @return
     */
    @RequestMapping("/reload")
    public Result reload(String logId, String orderId) {
        // 1) 调用微信关闭旧订单
        Map map = weixinPayService.close(logId);
        // 如果抛出异常或者服务器返回异常结果,都当做网络异常处理
        if (map == null || map.get("return_code").equals("FAIL")) {
            return new Result(false, "关闭失败,网络异常", 2); //
        } else {
            // 2) 如果关闭失败支付成功
            String result_code = (String) map.get("result_code");
            String err_code = (String) map.get("err_code");
            if ("FAIL".equals(result_code) && "ORDERPAID".equals(err_code)) {
                Map<String, String> mapsucc = weixinPayService.queryPayStatus(logId);
                orderService.updateOrderStatus(orderId, mapsucc.get("transaction_id"), logId);
                return new Result(false, "关闭失败,支付成功", 1);// 支付成功
                //
            } else {//只要不是支付成功都当做关闭成功处理
                // 1)更改旧订单状态 为关闭
                PayLog log = payLogService.updateStatus(logId, "2", null, null);
                //重新生成新的交易记录
                String newlogId = PayUtil.getId("02"); // 获取日志表号码
                log.setLogid(newlogId);
                log.setOuttradeno(newlogId);
                log.setTradestate("0");// 未支付
                log.setCreatetime(new Date());
                log.setPaytime(null);
                payLogService.add(log);

                // 2)重新生成二维码
                Map<String, String> newMap = weixinPayService.createNative(newlogId, log.getTotalfee() + "");
                // 3)重新生成 payLog
                newMap.put("paymenttype", "1");
                newMap.put("orderId", orderId);
                newMap.put("logId", newlogId);
                // 4) 生成网页二维码基本信息,并返回
                Result result = new Result(true, "二维码重新生成成功", newMap);
                return result;
            }
        }
    }


    /**
     * 支付宝查询订单状态
     * @param logId
     * @param orderId
     * @return
     */
    // 查询订单状态
    @RequestMapping("/findAliPayStatus")
    public Result findAliPayStatus(String logId, String orderId) {
        Result result = null;
        int x = 0;
        while (true) {
            Map<String, String> map = aliPayService.queryPayStatus(logId);//调用查询
            if (map == null) {
                result = new Result(false, "支付发生错误", "2");
                break;
            }
            if ("TRADE_SUCCESS".equals(map.get("trade_status"))) {//支付成功
                result = new Result(true, "支付成功", "1");
                orderService.updateOrderStatus(orderId, map.get("trade_no"), logId);//修改订单状态
                break;
            }
            // 循环 100次 每次 3秒 共5分钟
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            x++;
            if (x >= 4) {
                result = new Result(false, "二维码超时", "3");
                break;
            }

        }
        return result;
    }

    /**
     * 支付宝关闭订单
     * @param logId
     * @param orderId
     * @return
     */
    @RequestMapping("/alipayreload")
    public Result alireload(String logId, String orderId) {
        // 0)关闭一次订单
        aliPayService.close(logId);
        // 1) 查询一次是否成功
        Map<String, String> map = aliPayService.queryPayStatus(logId);//调用查询

        if ("TRADE_SUCCESS".equals(map.get("trade_status"))) {//支付成功
            orderService.updateOrderStatus(orderId, map.get("trade_no"), logId);//修改订单状态
            return new Result(false, "关闭失败,支付成功", 1);// 支付成功
        }
        // 2 关闭订单状态
        PayLog log = payLogService.updateStatus(logId, "2", null, null);
        Order order = orderService.findByid(log.getOrderid());
        //重新生成新的交易记录
        String newlogId = PayUtil.getId("02"); // 获取日志表号码
        log.setLogid(newlogId);
        log.setOuttradeno(newlogId);
        log.setTradestate("0");// 未支付
        log.setCreatetime(new Date());
        log.setPaytime(null);
        payLogService.add(log);

        // 2)重新生成二维码
        Map<String, String> newMap = aliPayService.createNative(newlogId, order.getPayment() + "", order.getGoodsid());
        // 3)重新生成 payLog
        newMap.put("paymenttype", "2");
        newMap.put("orderId", orderId);
        newMap.put("logId", newlogId);
        newMap.put("total_fee", order.getPayment() + "");
        // 4) 生成网页二维码基本信息,并返回
        Result result = new Result(true, "二维码重新生成成功", newMap);
        return result;
    }


}
