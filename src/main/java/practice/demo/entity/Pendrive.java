package practice.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pendrive_master")
public class Pendrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String serialNumber;   // 🔑 UNIQUE KEY

    @Column(nullable = false)
    private String labelName;      // OWNER MANUAL NAME

    private boolean active = true;

    private Integer insertCount = 1;

    private LocalDateTime createdAt;
}
