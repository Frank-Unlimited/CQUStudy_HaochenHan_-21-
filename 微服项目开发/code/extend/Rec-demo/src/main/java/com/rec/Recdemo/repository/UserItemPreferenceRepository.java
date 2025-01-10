package com.rec.Recdemo.repository;

import com.rec.Recdemo.model.UserItemPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserItemPreferenceRepository extends JpaRepository<UserItemPreference, Long> {
    List<UserItemPreference> findByUserId(String userId);

    Optional<UserItemPreference> findByUserIdAndItemId(String userId, String itemId);
}
