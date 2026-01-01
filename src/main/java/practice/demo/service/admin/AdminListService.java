package practice.demo.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.demo.dto.admin.AdminListResponse;
import practice.demo.dto.admin.AdminResponse;
import practice.demo.entity.User;
import practice.demo.repository.UserRepository;
import practice.demo.security.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminListService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 🔐 ONLY SUPER_ADMIN
    public AdminListResponse getAllAdmins(String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new AdminListResponse("Token missing", List.of());
        }

        String token = authorizationHeader.replace("Bearer ", "");

        try {
            if (jwtUtil.isTokenExpired(token)) {
                return new AdminListResponse("Token expired", List.of());
            }

            String email = jwtUtil.extractEmail(token);
            Integer tokenVersion = jwtUtil.extractTokenVersion(token);

            User loggedUser = userRepository.findByEmail(email).orElse(null);
            if (loggedUser == null ||
                    loggedUser.getTokenVersion() != tokenVersion) {

                return new AdminListResponse("Invalid token", List.of());
            }

            // 👑 ONLY SUPER ADMIN
            if (!"SUPER_ADMIN".equalsIgnoreCase(loggedUser.getRole())) {
                return new AdminListResponse("Access denied", List.of());
            }

            List<AdminResponse> admins = userRepository.findAll()
                    .stream()
                    .filter(user ->
                            "ADMIN".equalsIgnoreCase(user.getRole()) ||
                                    "SUPER_ADMIN".equalsIgnoreCase(user.getRole()))
                    .map(admin -> new AdminResponse(
                            admin.getId(),
                            admin.getEmail(),
                            admin.getRole(),
                            admin.getCreatedAt()
                    ))
                    .collect(Collectors.toList());

            return new AdminListResponse("Admins fetched successfully", admins);

        } catch (Exception e) {
            return new AdminListResponse("Token invalid", List.of());
        }
    }
}
