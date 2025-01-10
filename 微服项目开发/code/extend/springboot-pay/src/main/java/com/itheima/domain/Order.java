package com.itheima.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
@TableName("t_order")
public class Order {
    @TableId
    private String orderid;

    private double payment;

    private String paymenttype;

    private String postfee;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createtime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatetime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paymenttime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date closetime;

    private String userid;

    private String buyermessage;

    private String buyernick;

    private String receiverareaname;

    private String receivermobile;

    private String receiverzipcode;

    private String receiver;

    private String sellerid;

    private String goodsid;
}