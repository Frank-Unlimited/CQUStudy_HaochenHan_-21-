package com.itheima.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.github.wxpay.sdk.WXPayUtil;
import com.itheima.service.OrderService;
import com.itheima.util.AlipayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/pay")
public class CallbackController {
    @Autowired
    private OrderService orderService;

    /**
     * 微信支付回调
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/callback")
    public String callback(HttpServletRequest request) throws Exception {
        // 1获取信息
        String xmlRet=getXMLString(request);
        //        // 打印
            System.out.println(("==================================00000000000000000000000000000000"));
            System.out.println(xmlRet);
            System.out.println(("==================================00000000000000000000000000000000"));
        // 2处理业务
        boolean callback = orderService.WXSuccessCallback(xmlRet);
        Map map = new HashMap();
        // 3如果处理成功
        if(callback) {
            map.put("return_code", "SUCCESS");
            map.put("return_msg", "OK");
        }else {
            //4 如果失败
            map.put("return_code", "FAIL");
            map.put("return_msg", "fail");
        }

        return WXPayUtil.mapToXml(map);


    }

    /**
     * 解析 请求的xml
     * @param request
     * @return
     */
    private  String getXMLString (HttpServletRequest request){
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        try {
            inputStream = request.getInputStream();
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
        return  sb.toString();
    }

    /**
     * 阿里支付回调
     * @param request
     * @throws Exception
     */
    @RequestMapping("/alicallback")
    public void alicallback(HttpServletRequest request) throws Exception {
        System.out.println("================aliallilllllllliii=================");
        Map<String,String> params = pareReq(request);
        String signType=params.get("sign_type");
        //验签操作
        boolean b = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, "UTF-8", signType);
        System.out.println(b);
    }

    private Map<String,String> pareReq(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String,String> map = new HashMap<>();
        for (Map.Entry<String, String[]> entry :parameterMap.entrySet()){
           String key = entry.getKey();
           String  value =entry.getValue()[0];
           map.put(key,value);
        }
        return map;
    }






}
