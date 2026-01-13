package practice.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.demo.entity.EmergencyContact;

import java.util.List;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {

    List<EmergencyContact> findByUserProfileUserEmail(String email);

    List<EmergencyContact> findByUserProfileId(Long userProfileId);   // ✅ Add this
}
