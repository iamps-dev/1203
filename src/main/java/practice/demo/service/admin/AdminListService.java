package practice.demo.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.demo.dto.admin.AdminResponse;
import practice.demo.entity.User;
import practice.demo.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminListService {

    @Autowired
    private UserRepository userRepository;

    // 🔹 Fetch all admins (ADMIN + SUPER_ADMIN)
    public List<AdminResponse> getAllAdmins() {

        return userRepository.findAll()
                .stream()
                .filter(user -> "ADMIN".equals(user.getRole()) || "SUPER_ADMIN".equals(user.getRole()))
                .map(admin -> new AdminResponse(
                        admin.getId(),
                        admin.getEmail(),
                        admin.getRole(),
                        admin.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
