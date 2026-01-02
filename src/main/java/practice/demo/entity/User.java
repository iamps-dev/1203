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

    @Column(nullable = false)
    private int tokenVersion = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "password_changed_at", nullable = false)
    private LocalDateTime passwordChangedAt;

    // ✅ Auto set on insert
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.passwordChangedAt = now;
    }

    // ================= GETTERS =================
    public Long getId() { return id; }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public String getRole() { return role; }

    public int getTokenVersion() { return tokenVersion; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getPasswordChangedAt() { return passwordChangedAt; }

    // ================= SETTERS =================
    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setTokenVersion(int tokenVersion) {
        this.tokenVersion = tokenVersion;
    }

    // 🔐 IMPORTANT: PASSWORD SETTER
    public void setPassword(String password) {
        this.password = password;
        this.passwordChangedAt = LocalDateTime.now(); // ✅ update time
    }
}
