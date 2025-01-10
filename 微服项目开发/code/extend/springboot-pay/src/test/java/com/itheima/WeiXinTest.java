package com.itheima;

import com.itheima.service.WeixinPayService;
import com.itheima.service.impl.WeixinPayServiceImpl;
import org.junit.Test;

import java.util.Map;

public class WeiXinTest {
    @Test
    public void test(){
        WeixinPayService weixinPayService = new WeixinPayServiceImpl();
        Map aNative = weixinPayService.createNative("1231235555555555588881", "1");
        System.out.println(aNative);
    }

    @Test
    public void tes1t(){
        WeixinPayService weixinPayService = new WeixinPayServiceImpl();
        Map map = weixinPayService.queryPayStatus("1231235555555555588881");
        System.out.println(map);
    }

    @Test
    public void tes2(){
        WeixinPayService weixinPayService = new WeixinPayServiceImpl();
        Map map = weixinPayService.close("1231235555555555588881");
        System.out.println(map);
    }
}
