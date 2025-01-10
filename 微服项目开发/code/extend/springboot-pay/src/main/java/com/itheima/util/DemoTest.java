package com.itheima.util;

public class DemoTest {
    public static void main(String[] args) throws Exception {
        HttpClient httpClient = new HttpClient("https://www.baidu.com");
        httpClient.setHttps(true);
        httpClient.post();
        int statusCode = httpClient.getStatusCode();
        System.out.println(statusCode);
        String content = httpClient.getContent();
        System.out.println(content);

    }
}
