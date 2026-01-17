package practice.demo.service.users;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.user.SOSRequest;
import practice.demo.entity.EmergencyContact;
import practice.demo.entity.User;
import practice.demo.entity.UserProfile;
import practice.demo.repository.EmergencyContactRepository;
import practice.demo.repository.UserProfileRepository;
import practice.demo.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SOSService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final EmergencyContactRepository emergencyContactRepository;
    private final JavaMailSender mailSender;

    public ApiResponse sendSOS(String email, SOSRequest request) {

        // ✅ Find user by email (from JWT)
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ApiResponse.error("User not found");
        }

        // ✅ Fetch user profile
        UserProfile profile = userProfileRepository.findByUserId(user.getId());
        if (profile == null) {
            return ApiResponse.error("User profile not found");
        }

        // ✅ Fetch primary emergency contact
        EmergencyContact primaryContact =
                emergencyContactRepository
                        .findFirstByUserProfileIdAndIsPrimaryTrue(profile.getId());

        if (primaryContact == null) {
            return ApiResponse.error("Primary emergency contact not found");
        }

        if (primaryContact.getEmail() == null || primaryContact.getEmail().isBlank()) {
            return ApiResponse.error("Primary emergency contact email missing");
        }

        // ✅ Google Maps location link
        String locationLink =
                "https://www.google.com/maps/search/?api=1&query="
                        + request.getLatitude() + "," + request.getLongitude();

        // ✅ Send SOS email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(primaryContact.getEmail());
        message.setSubject("🚨 SOS Alert");
        message.setText(
                "🚨 SOS ALERT 🚨\n\n" +
                        "User: " + profile.getFullName() + "\n" +
                        "Live Location:\n" + locationLink
        );

        mailSender.send(message);

        return ApiResponse.success(
                "SOS alert sent to " + primaryContact.getName()
        );
    }
}
