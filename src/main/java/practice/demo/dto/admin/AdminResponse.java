package practice.demo.dto.admin;

public class AdminResponse {

    private Long id;
    private String email;

    // 🔹 Constructor
    public AdminResponse(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    // 🔹 Getters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // 🔹 Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
