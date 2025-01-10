package com.rec.Recdemo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_item_preference")
public class UserItemPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "preference")
    private int preference;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId.toUpperCase(); // 强制转换为大写
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId.toLowerCase(); // 强制转换为小写
    }

    public int getPreference() {
        return preference;
    }

    public void setPreference(int preference) {
        this.preference = preference;
    }
}
