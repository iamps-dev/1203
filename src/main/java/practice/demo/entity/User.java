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

    // Renamed from tokenVersion -> passwordVersion
    @Column(name = "password_version", nullable = false)
    private int passwordVersion = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "password_changed_at", nullable = false)
    private LocalDateTime passwordChangedAt;

    // =================== Lifecycle Hooks ===================
    // ✅ Auto set on insert
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.passwordChangedAt = now;
    }

    // =================== GETTERS ===================
    public Long getId() { return id; }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public String getRole() { return role; }

    public int getPasswordVersion() { return passwordVersion; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getPasswordChangedAt() { return passwordChangedAt; }

    // =================== SETTERS ===================
    public void setEmail(String email) { this.email = email; }

    public void setRole(String role) { this.role = role; }

    public void setPasswordVersion(int passwordVersion) { this.passwordVersion = passwordVersion; }

    // 🔐 IMPORTANT: PASSWORD SETTER
    public void setPassword(String password) {
        this.password = password;
        this.passwordChangedAt = LocalDateTime.now(); // update timestamp
        this.passwordVersion++; // increment version automatically
    }
}
