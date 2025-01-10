package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.domain.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    //@Select("select * from t_order")
    //public List<Order> findAll();
    //
    //@Select("insert into t_order (orderid, payment, paymenttype, \n" +
    //        "      postfee, status, createtime, \n" +
    //        "      updatetime, paymenttime, closetime, \n" +
    //        "       userid, buyermessage, \n" +
    //        "      buyernick, receiverareaname, receivermobile, \n" +
    //        "      receiverzipcode, receiver, sellerid, \n" +
    //        "      goodsid)\n" +
    //        "    values (#{orderid}, #{payment}, #{paymenttype}, \n" +
    //        "      #{postfee}, #{status}, #{createtime}, \n" +
    //        "      #{updatetime}, #{paymenttime}, #{closetime}, \n" +
    //        "       #{userid}, #{buyermessage}, \n" +
    //        "      #{buyernick}, #{receiverareaname}, #{receivermobile}, \n" +
    //        "      #{receiverzipcode}, #{receiver}, #{sellerid}, \n" +
    //        "      #{goodsid})")
    //public void add(Order order);
    //
    //@Update("update t_order set status=#{status} " +
    //        ",updatetime=#{updatetime}" +
    //        ", closetime=#{closetime} " +
    //        ", paymenttime=#{paymenttime} " +
    //        "where orderid=#{orderid}")
    //void updateOrder(Order order);
    //
    //
    //
    //@Select("select * from t_order  where orderid=#{orderid}")
    //public Order findById(String orderid);
}
