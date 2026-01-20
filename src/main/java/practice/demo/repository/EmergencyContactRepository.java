package practice.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.demo.entity.EmergencyContact;
import java.util.List;

public interface EmergencyContactRepository
        extends JpaRepository<EmergencyContact, Long> {

    // 🔹 Get all emergency contacts of user
    List<EmergencyContact> findByUserProfileId(Long userProfileId);

    // 🔹 Get any one primary contact
    EmergencyContact findFirstByUserProfileIdAndIsPrimaryTrue(Long userProfileId);

    // 🔹 COUNT how many primary contacts exist (IMPORTANT)
    long countByUserProfileIdAndIsPrimaryTrue(Long userProfileId);
}
