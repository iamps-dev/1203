package practice.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.demo.entity.OtpVerification;
import practice.demo.entity.User;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findByUser(User user);

    Optional<OtpVerification> findByUserAndVerifiedFalse(User user);
}
