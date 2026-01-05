package practice.demo.dto.admin;

public class UpdateAdminRequest {

    private Long adminId;
    private String newEmail;
    private String newPassword;
    private Boolean isActive; // ✅ Added this field

    public UpdateAdminRequest() {}

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Boolean getIsActive() { // ✅ Getter
        return isActive;
    }

    public void setIsActive(Boolean isActive) { // ✅ Setter
        this.isActive = isActive;
    }
}
