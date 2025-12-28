package practice.demo.controller.admin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import practice.demo.entity.User;
import practice.demo.repository.UserRepository;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner createDefaultSuperAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            String superAdminEmail = "superadmin@gmail.com";

            if (userRepository.findByEmail(superAdminEmail).isEmpty()) {

                User superAdmin = new User();
                superAdmin.setEmail(superAdminEmail);
                superAdmin.setPassword(passwordEncoder.encode("super123"));
                superAdmin.setRole("SUPER_ADMIN");

                userRepository.save(superAdmin);
                System.out.println("✅ Default SUPER_ADMIN created");
            } else {
                System.out.println("ℹ️ SUPER_ADMIN already exists");
            }
        };
    }
}
