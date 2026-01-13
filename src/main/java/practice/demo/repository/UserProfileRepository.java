package practice.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.demo.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // ✅ Find profile by user ID
    UserProfile findByUserId(Long userId);
}
