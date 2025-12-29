package practice.demo.dto.admin;

import java.time.LocalDateTime;

public class AdminResponse {

    private Long id;
    private String email;
    private String role;
    private LocalDateTime createdAt;

    // Constructor
    public AdminResponse(Long id, String email, String role, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
