package practice.demo.dto.user;

public class AddEmergencyContactRequest {
    private String name;
    private String phone;
    private String email;
    private Boolean isPrimary; // true if this should be the primary contact

    // GETTERS & SETTERS
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }
}
