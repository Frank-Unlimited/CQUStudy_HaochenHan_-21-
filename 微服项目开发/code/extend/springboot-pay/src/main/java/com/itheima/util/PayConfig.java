package com.itheima.util;

/**
 * 微信支付相关参数
 */
public class PayConfig {
	//企业方公众号Id
	public static String appid = "wxb709cf6e6a7d9d2a";
	public static String appSecret = "d9a9ff00a633cd7353a8925119063b01";
	//财付通平台的商户账号
	public static String partner = "1473426802";
	//财付通平台的商户密钥
	public static String partnerkey = "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb";
	//回调URL(必须提供,但是实际上扫码支付用不到)
	public static String notifyurl = "http://hhc.vs4.tunnelfrp.com/pay/callback";
}
