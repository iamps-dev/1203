package practice.demo.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class allAdminResponse {

    // 🔹 Common API message
    private String message;

    // 🔹 Admin list
    private List<AdminData> admins;

    // ================= INNER DTO =================
    @Getter
    @AllArgsConstructor
    public static class AdminData {

        private Long id;
        private String email;
        private String role;
        private LocalDateTime createdAt;

        // 🔐 Security
        private int passwordVersion;
        private LocalDateTime passwordChangedAt;

        // ✅ Active status
        private boolean isActive; // <--- added
    }
}
