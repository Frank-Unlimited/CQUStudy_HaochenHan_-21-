package com.example.demo.controller;

import com.example.demo.service.AmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MapController {

    @Autowired
    private AmapService amapService;

    // 页面映射：将根路径映射到 cart.html
    @GetMapping("/")
    public String home() {
        return "cart"; // 返回模板页面：src/main/resources/templates/cart.html
    }

    @GetMapping("/getAddress")
    @ResponseBody
    public String getAddress(@RequestParam double lat, @RequestParam double lon) {
        return amapService.getAddressFromCoordinates(lat, lon);
    }
}
