package practice.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import practice.demo.entity.Pendrive;

import java.util.Optional;

@Repository
public interface PendriveRepository extends JpaRepository<Pendrive, Long> {

    boolean existsBySerialNumber(String serialNumber);

    Optional<Pendrive> findBySerialNumber(String serialNumber);
}
