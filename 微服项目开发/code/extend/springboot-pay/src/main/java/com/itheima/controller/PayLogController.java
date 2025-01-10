package com.itheima.controller;

import com.itheima.domain.PayLog;
import com.itheima.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/paylog")
public class PayLogController {
    @Autowired
    private PayLogService payLogService;

    @RequestMapping("/findAll")
    public List<PayLog> findAll(){
        return payLogService.findAll();
    }


}
