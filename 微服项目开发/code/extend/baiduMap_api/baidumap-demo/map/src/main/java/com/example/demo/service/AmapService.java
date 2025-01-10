package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class AmapService {

    @Autowired
    private com.example.demo.config.AmapConfig amapConfig;

    private final RestTemplate restTemplate;

    public AmapService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 调用高德 API 获取地址信息
    public String getAddressFromCoordinates(double lat, double lon) {
        String url = String.format(
                "https://restapi.amap.com/v3/geocode/regeo?key=%s&location=%f,%f",
                amapConfig.getApiKey(), lon, lat
        );

        try {
            // 调用高德 API
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // 打印返回的 JSON 数据以便调试
            System.out.println("高德API返回结果: " + response.getBody());

            // 解析地址信息
            return parseAddress(response.getBody());
        } catch (Exception e) {
            // 捕获异常并返回提示
            e.printStackTrace();
            return "无法获取地址信息，请稍后再试。";
        }
    }

    // 解析 JSON，提取 "formatted_address" 字段
    private String parseAddress(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper(); // 使用 Jackson 解析 JSON
            JsonNode root = objectMapper.readTree(jsonResponse);

            // 检查 status 是否为 "1" 表示成功
            String status = root.path("status").asText();
            if (!"1".equals(status)) {
                String info = root.path("info").asText();
                return "高德API请求失败，错误信息：" + info;
            }

            // 提取 "formatted_address"
            JsonNode regeocode = root.path("regeocode");
            JsonNode formattedAddress = regeocode.path("formatted_address");

            if (formattedAddress.isMissingNode() || formattedAddress.asText().isEmpty()) {
                return "未能找到有效的地址信息";
            }

            return formattedAddress.asText(); // 返回解析到的地址
        } catch (Exception e) {
            e.printStackTrace();
            return "地址解析失败";
        }
    }
}
