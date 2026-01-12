package practice.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.demo.entity.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);
}
