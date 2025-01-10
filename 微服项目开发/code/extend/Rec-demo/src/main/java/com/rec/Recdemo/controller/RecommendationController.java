package com.rec.Recdemo.controller;

import com.rec.Recdemo.service.RecommendationService;
import com.rec.Recdemo.model.UserItemPreference;
import com.rec.Recdemo.repository.UserItemPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserItemPreferenceRepository userItemPreferenceRepository;

    // 获取推荐商品
    @GetMapping("/recommendation/{userId}")
    public ResponseEntity<Map<String, Double>> getRecommendations(@PathVariable String userId) {
        Map<String, Double> recommendations = recommendationService.recommendItems(userId);
        return ResponseEntity.ok(recommendations);
    }

    // 记录用户喜欢某个商品
    @PostMapping("/like/{itemId}")
    public ResponseEntity<String> likeItem(@PathVariable String itemId) {
        try {
            // 假设当前用户 ID 是 "A"，可以根据需求动态获取用户 ID
            String userId = "A";

            // 检查数据库中是否已有记录
            Optional<UserItemPreference> existingPreference = userItemPreferenceRepository.findByUserIdAndItemId(userId, itemId);
            if (existingPreference.isPresent()) {
                // 如果记录存在，更新喜好为 1
                UserItemPreference preference = existingPreference.get();
                preference.setPreference(1);
                userItemPreferenceRepository.save(preference);
            }
//            else {
//                // 如果记录不存在，创建新记录
//                UserItemPreference preference = new UserItemPreference();
//                preference.setUserId(userId);
//                preference.setItemId(itemId);
//                preference.setPreference(1);
//                userItemPreferenceRepository.save(preference);
//            }
            return ResponseEntity.ok("记录成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("记录失败");
        }
    }
}
