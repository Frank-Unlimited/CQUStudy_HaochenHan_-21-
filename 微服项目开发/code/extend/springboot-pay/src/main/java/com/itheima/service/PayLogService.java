package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.PayLog;

import java.util.Date;
import java.util.List;

public interface PayLogService extends IService<PayLog> {
    public List<PayLog> findAll();

    PayLog findById(String logId);

    PayLog updateStatus(String logId, String status, String transactionid, Date payTime);

    void add(PayLog log);

}
