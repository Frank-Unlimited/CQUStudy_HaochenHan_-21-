package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.domain.PayLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayLogMapper extends BaseMapper<PayLog> {
    //@Select("select * from t_pay_log")
    //public List<PayLog> findAll();
    //
    //@Insert(" insert into t_pay_log (logid,outtradeno, createtime, paytime, \n" +
    //        "      totalfee, userid, transactionid, \n" +
    //        "      tradestate, orderid, paytype)" +
    //        "    values (#{logid},#{outtradeno}, #{createtime}, #{paytime}, \n" +
    //        "      #{totalfee}, #{userid}, #{transactionid}, \n" +
    //        "      #{tradestate}, #{orderid}, #{paytype}\n" +
    //        "      )")
    //public void add(PayLog payLog);
    //@Select("select * from t_pay_log where logid=#{logid}")
    //public PayLog findById(String logId);
    //@Update("update t_pay_log set paytime=#{paytime}, transactionid=#{transactionid} , tradestate= #{tradestate}where logid=#{logid}")
    //void update(PayLog log);
}
