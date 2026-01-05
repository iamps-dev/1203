package practice.demo.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.admin.*;
import practice.demo.entity.User;
import practice.demo.repository.UserRepository;
import practice.demo.security.JwtUtil;

@Service
public class AdminAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ================= LOGIN =================
    public ApiResponse login(AdminLoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) return new ApiResponse(false, "User not found");

        if (!user.isActive())
            return new ApiResponse(false, "Account deactivated by Super Admin");

        if (!("ADMIN".equalsIgnoreCase(user.getRole())
                || "SUPER_ADMIN".equalsIgnoreCase(user.getRole()))) {
            return new ApiResponse(false, "Access denied");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            return new ApiResponse(false, "Invalid credentials");

        String token = jwtUtil.generateAdminToken(user);
        return new ApiResponse(true, "Login successful", token);
    }

    // ================= CREATE ADMIN =================
    public ApiResponse createAdmin(String token, CreateAdminRequest request) {

        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        User superAdmin = userRepository.findByEmail(email).orElse(null);

        if (superAdmin == null || !"SUPER_ADMIN".equalsIgnoreCase(superAdmin.getRole()))
            return new ApiResponse(false, "Only Super Admin can create admins");

        if (userRepository.findByEmail(request.getEmail()).isPresent())
            return new ApiResponse(false, "Email already exists");

        User admin = new User();
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole("ADMIN");
        admin.setActive(true); // always active when created

        userRepository.save(admin);
        return new ApiResponse(true, "Admin created successfully");
    }

    // ================= UPDATE ADMIN =================
    public ApiResponse updateAdmin(UpdateAdminRequest request) {

        User admin = userRepository.findById(request.getAdminId()).orElse(null);

        if (admin == null || "SUPER_ADMIN".equalsIgnoreCase(admin.getRole()))
            return new ApiResponse(false, "Admin not found");

        userRepository.findByEmail(request.getNewEmail()).ifPresent(existing -> {
            if (!existing.getId().equals(admin.getId()))
                throw new RuntimeException("Email already in use");
        });

        admin.setEmail(request.getNewEmail());
        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(admin);

        return new ApiResponse(true, "Admin updated successfully");
    }

    // ================= TOGGLE ADMIN ACTIVE/INACTIVE =================
    public ApiResponse changeAdminStatus(String token, UpdateAdminStatusRequest request) {

        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        User superAdmin = userRepository.findByEmail(email).orElse(null);

        if (superAdmin == null || !"SUPER_ADMIN".equalsIgnoreCase(superAdmin.getRole()))
            return new ApiResponse(false, "Only Super Admin allowed");

        User admin = userRepository.findById(request.getAdminId()).orElse(null);

        if (admin == null || "SUPER_ADMIN".equalsIgnoreCase(admin.getRole()))
            return new ApiResponse(false, "Invalid admin");

        // 🔥 toggle active/inactive + force logout
        admin.setActive(request.isActive());
        userRepository.save(admin);

        return new ApiResponse(
                true,
                request.isActive()
                        ? "Admin activated successfully"
                        : "Admin deactivated & logged out"
        );
    }
}
