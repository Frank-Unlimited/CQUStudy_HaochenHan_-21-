package com.zx.order.controller;

import com.zx.domain.vo.common.Result;
import com.zx.order.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("order")
public class PaymentInfoController {

    @Autowired
    private PaymentInfoService paymentInfoService;

    @GetMapping("alipay/submitAlipay/{orderNo}")
    public Result<String> submitAlipay(@PathVariable("orderNo") String orderNo) {
        return paymentInfoService.submitAlipay(orderNo);
    }

    @PostMapping("alipay/callback/notify")
    public String test(@RequestParam Map<String, String> paramsMap){
        System.out.println("--------------------------"+paramsMap);
        paymentInfoService.callback(paramsMap);
        return "SUCCESS";
    }

    @GetMapping("alipay/show")
    public String ret(){
        return "SUCCESS";
    }

}
