package practice.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    // JWT invalidation
    @Column(name = "password_version", nullable = false)
    private int passwordVersion = 0;

    // ✅ FIXED: force MySQL to use TINYINT(1)
    @Column(
            name = "is_active",
            nullable = false,
            columnDefinition = "TINYINT(1)"
    )
    private Boolean isActive;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "password_changed_at", nullable = false)
    private LocalDateTime passwordChangedAt;

    // =====================
    // LIFECYCLE
    // =====================
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.passwordChangedAt = now;

        // ✅ default only on create
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    // =====================
    // GETTERS
    // =====================
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public int getPasswordVersion() { return passwordVersion; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getPasswordChangedAt() { return passwordChangedAt; }

    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    // =====================
    // SETTERS
    // =====================
    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
        this.passwordChangedAt = LocalDateTime.now();
        this.passwordVersion++;
    }

    public void setActive(Boolean active) {
        this.isActive = active;
        this.passwordVersion++; // force logout
    }
}
