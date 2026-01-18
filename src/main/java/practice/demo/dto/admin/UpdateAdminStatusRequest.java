package practice.demo.dto.admin;

public class UpdateAdminStatusRequest {
    private Long adminId;
    private boolean active; // true = activate, false = deactivate

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}