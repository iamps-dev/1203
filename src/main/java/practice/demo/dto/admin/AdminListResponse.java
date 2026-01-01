package practice.demo.dto.admin;

import java.util.List;

public class AdminListResponse {

    private String message;
    private List<AdminResponse> data;

    public AdminListResponse(String message, List<AdminResponse> data) {
        this.message = message;
        this.data = data;
    }

    // Getters and setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<AdminResponse> getData() { return data; }
    public void setData(List<AdminResponse> data) { this.data = data; }
}
