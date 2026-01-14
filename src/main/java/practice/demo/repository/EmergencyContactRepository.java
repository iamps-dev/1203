package practice.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.demo.entity.EmergencyContact;
import java.util.List;

public interface    EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {

    // ✅ Get all contacts (optional)
    List<EmergencyContact> findByUserProfileId(Long userProfileId);

    // ✅ Get primary contact only
    EmergencyContact findFirstByUserProfileIdAndIsPrimaryTrue(Long userProfileId);
}
