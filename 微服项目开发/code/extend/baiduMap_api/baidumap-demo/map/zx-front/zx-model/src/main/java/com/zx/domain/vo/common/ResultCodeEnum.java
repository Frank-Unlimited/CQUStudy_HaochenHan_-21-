package com.zx.domain.vo.common;

import lombok.Getter;

@Getter // 提供获取属性值的getter方法
public enum ResultCodeEnum {

    SUCCESS(200 , "操作成功") ,
    USER_NON_EXISTS(211,"查无此用户"),
    LOGIN_ERROR(201 , "密码错误"),
    VALIDATECODE_ERROR(202 , "验证码错误") ,
    LOGIN_AUTH(208 , "用户未登录"),
    USER_NAME_IS_EXISTS(209 , "用户已存在"),
    SYSTEM_ERROR(9999 , "您的网络有问题请稍后重试"),
    NODE_ERROR( 217, "该节点下有子节点，不可以删除"),
    DATA_ERROR(204, "数据异常"),
    ACCOUNT_STOP( 216, "账号已停用"),
    ORDER_EXISTS( 218, "订单已存在或订单号过期,请刷新页面重试"),
    STOCK_LESS( 219, "库存不足"),
    DATA_NOT_EXISTS( 220, "查无数据"),

    PARAM_ERROR(9221, "验证错误"),
    RE_REGISTERED(9222, "用户已存在"),
    TIMEOUT_VALIDATION(9223, "验证码失效"),
    HAVE_COLLECEED(9224, "商品已收藏");

    private Integer code ;      // 业务状态码
    private String message ;    // 响应消息

    private ResultCodeEnum(Integer code , String message) {
        this.code = code ;
        this.message = message ;
    }

}
