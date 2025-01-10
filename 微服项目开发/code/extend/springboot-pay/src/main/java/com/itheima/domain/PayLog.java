package com.itheima.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
@TableName("t_pay_log")
public class PayLog {

    @TableId
    private String logid;

    private String outtradeno;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createtime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paytime;

    private int totalfee;

    private String userid;

    private String transactionid;

    private String tradestate;

    private String orderid;

    private String paytype;

}