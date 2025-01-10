package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.domain.PayLog;
import com.itheima.mapper.PayLogMapper;
import com.itheima.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private PayLogMapper payLogMapper;

    @Override
    public List<PayLog> findAll(){
        //return payLogMapper.findAll();
        return payLogMapper.selectList(null);
    }

    @Override
    public PayLog findById(String logId) {
        //return payLogMapper.findById(logId);
        return payLogMapper.selectById(logId);

    }
    @Override
    public PayLog updateStatus(String logId, String status, String transactionid, Date payTime){
        //PayLog log= payLogMapper.findById(logId);
        PayLog log= payLogMapper.selectById(logId);
        log.setTransactionid(transactionid);
        log.setTradestate(status);
        log.setPaytime(payTime);
        //payLogMapper.update(log);
        payLogMapper.updateById(log);
        return  log;
    }

    @Override
    public void add(PayLog log) {
        //payLogMapper.add(log);
        payLogMapper.insert(log);
    }

}
