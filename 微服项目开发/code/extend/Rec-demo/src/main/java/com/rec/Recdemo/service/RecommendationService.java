
package com.rec.Recdemo.service;

import com.rec.Recdemo.model.UserItemPreference;
import com.rec.Recdemo.repository.UserItemPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationService {

    @Autowired
    private UserItemPreferenceRepository repository;

    public Map<String, Double> recommendItems(String userId) {
        // Step 1: 获取所有用户的喜好数据
        List<UserItemPreference> allPreferences = repository.findAll();
        System.out.println("Step 1: 所有用户的偏好数据:");
        for (UserItemPreference pref : allPreferences) {
            System.out.println(pref);
        }

        // Step 2: 构建用户-物品的倒排表
        Map<String, List<String>> userToItems = new HashMap<>();
        for (UserItemPreference pref : allPreferences) {
            if (pref.getPreference() == 1) { // 只考虑用户明确喜欢的商品
                userToItems.computeIfAbsent(pref.getUserId(), k -> new ArrayList<>()).add(pref.getItemId());
            }
        }
        System.out.println("Step 2: 用户-物品倒排表:");
        for (Map.Entry<String, List<String>> entry : userToItems.entrySet()) {
            System.out.println("用户: " + entry.getKey() + " -> 喜欢的商品: " + entry.getValue());
        }

        // Step 3: 构建商品共现矩阵（C）和商品的流行度（N）
        Map<String, Map<String, Integer>> coOccurrenceMatrix = new HashMap<>();
        Map<String, Integer> itemPopularity = new HashMap<>();

        for (List<String> items : userToItems.values()) {
            for (int i = 0; i < items.size(); i++) {
                String itemA = items.get(i);
                itemPopularity.put(itemA, itemPopularity.getOrDefault(itemA, 0) + 1);
                for (int j = 0; j < items.size(); j++) {
                    if (i == j) continue; // 跳过自己
                    String itemB = items.get(j);
                    coOccurrenceMatrix
                            .computeIfAbsent(itemA, k -> new HashMap<>())
                            .put(itemB, coOccurrenceMatrix.get(itemA).getOrDefault(itemB, 0) + 1);
                }
            }
        }
        System.out.println("Step 3: 商品流行度:");
        for (Map.Entry<String, Integer> entry : itemPopularity.entrySet()) {
            System.out.println("商品: " + entry.getKey() + " -> 被喜欢的用户数: " + entry.getValue());
        }
        System.out.println("Step 3: 商品共现矩阵:");
        for (Map.Entry<String, Map<String, Integer>> entry : coOccurrenceMatrix.entrySet()) {
            System.out.println("商品: " + entry.getKey() + " -> 共现: " + entry.getValue());
        }

        // Step 4: 计算商品之间的相似度矩阵（W）
        Map<String, Map<String, Double>> similarityMatrix = new HashMap<>();
        for (String itemA : coOccurrenceMatrix.keySet()) {
            for (String itemB : coOccurrenceMatrix.get(itemA).keySet()) {
                int coCount = coOccurrenceMatrix.get(itemA).get(itemB);
                double sim = coCount / Math.sqrt(itemPopularity.get(itemA) * itemPopularity.get(itemB));
                similarityMatrix
                        .computeIfAbsent(itemA, k -> new HashMap<>())
                        .put(itemB, sim);
            }
        }
        System.out.println("Step 4: 商品相似度矩阵:");
        for (Map.Entry<String, Map<String, Double>> entry : similarityMatrix.entrySet()) {
            System.out.println("商品: " + entry.getKey() + " -> 相似商品: " + entry.getValue());
        }

        // Step 5: 根据用户历史记录生成推荐（只推荐未知的商品）
        Map<String, Double> recommendations = new HashMap<>();
        List<String> likedItems = userToItems.getOrDefault(userId, new ArrayList<>());
        System.out.println("Step 5: 用户 " + userId + " 喜欢的商品: " + likedItems);

        // 找到用户不确定（preference = -1）的商品
        for (UserItemPreference pref : allPreferences) {
            if (pref.getUserId().equals(userId) && pref.getPreference() == -1) {
                String targetItem = pref.getItemId(); // 不确定商品
                double predictedScore = 0.0;

                // 对用户明确喜欢的商品，计算与目标商品的相似度加权得分
                for (String likedItem : likedItems) {
                    Map<String, Double> similarItems = similarityMatrix.get(likedItem);
                    if (similarItems != null && similarItems.containsKey(targetItem)) {
                        predictedScore += similarItems.get(targetItem);
                    }
                }

                System.out.println("商品: " + targetItem + " -> 推荐得分: " + predictedScore);
                // 保存推荐分数
                recommendations.put(targetItem, predictedScore);
            }
        }

        // Step 6: 按得分排序并返回
        Map<String, Double> sortedRecommendations = recommendations.entrySet()
                .stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .collect(LinkedHashMap::new,
                        (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                        LinkedHashMap::putAll);
        System.out.println("Step 6: 最终推荐结果:");
        for (Map.Entry<String, Double> entry : sortedRecommendations.entrySet()) {
            System.out.println("商品: " + entry.getKey() + " -> 推荐得分: " + entry.getValue());
        }


        return sortedRecommendations;
    }
}


