package practice.demo.dto.admin;

import java.time.LocalDateTime;

public class AdminResponse {

    private Long id;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private int tokenVersion;

    // 🔐 NEW
    private LocalDateTime passwordChangedAt;

    public AdminResponse(
            Long id,
            String email,
            String role,
            LocalDateTime createdAt,
            int tokenVersion,
            LocalDateTime passwordChangedAt
    ) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.tokenVersion = tokenVersion;
        this.passwordChangedAt = passwordChangedAt;
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getTokenVersion() {
        return tokenVersion;
    }

    public LocalDateTime getPasswordChangedAt() {
        return passwordChangedAt;
    }
}
