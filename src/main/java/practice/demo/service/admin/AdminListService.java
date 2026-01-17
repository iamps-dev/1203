package practice.demo.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.demo.dto.admin.allAdminResponse;
import practice.demo.entity.User;
import practice.demo.repository.UserRepository;
import practice.demo.security.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class    AdminListService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 👑 ONLY SUPER_ADMIN
    public allAdminResponse getAllAdmins(String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new allAdminResponse("Authorization token missing", List.of());
        }

        String token = authorizationHeader.substring(7);

        try {
            if (jwtUtil.isTokenExpired(token)) {
                return new allAdminResponse("Token expired", List.of());
            }

            String email = jwtUtil.extractEmail(token);
            int tokenVersion = jwtUtil.extractTokenVersion(token);

            User loggedUser = userRepository.findByEmail(email).orElse(null);

            if (loggedUser == null || loggedUser.getPasswordVersion() != tokenVersion) {
                return new allAdminResponse("Invalid token", List.of());
            }

            if (!"SUPER_ADMIN".equalsIgnoreCase(loggedUser.getRole())) {
                return new allAdminResponse("Access denied", List.of());
            }

            List<allAdminResponse.AdminData> admins = userRepository.findAll()
                    .stream()
                    .filter(u ->
                            "ADMIN".equalsIgnoreCase(u.getRole()) ||
                                    "SUPER_ADMIN".equalsIgnoreCase(u.getRole()))
                    .map(u -> new allAdminResponse.AdminData(
                            u.getId(),
                            u.getEmail(),
                            u.getRole(),
                            u.getCreatedAt(),
                            u.getPasswordVersion(),
                            u.getPasswordChangedAt(),
                            u.isActive() // ✅ include this


                    ))
                    .collect(Collectors.toList());

            return new allAdminResponse("Admins fetched successfully", admins);

        } catch (Exception e) {
            return new allAdminResponse("Token invalid", List.of());
        }
    }
}
