    package practice.demo.service.users;

    import lombok.RequiredArgsConstructor;
    import org.springframework.mail.SimpleMailMessage;
    import org.springframework.mail.javamail.JavaMailSender;
    import org.springframework.stereotype.Service;
    import practice.demo.ApiResponse.ApiResponse;
    import practice.demo.dto.user.SOSRequest;
    import practice.demo.entity.EmergencyContact;
    import practice.demo.repository.EmergencyContactRepository;

    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class SOSService {

        private final EmergencyContactRepository emergencyContactRepository;
        private final JavaMailSender mailSender;

        public ApiResponse sendSOS(String userEmail, SOSRequest request) {

            List<EmergencyContact> contacts =
                    emergencyContactRepository.findByUserProfileUserEmail(userEmail);

            if (contacts.isEmpty()) {
                return ApiResponse.error("No emergency contacts found");
            }

            EmergencyContact primaryContact = contacts.stream()
                    .filter(c -> Boolean.TRUE.equals(c.getIsPrimary()))
                    .findFirst()
                    .orElseThrow(() ->
                            new RuntimeException("Primary emergency contact not found"));

            if (primaryContact.getEmail() == null || primaryContact.getEmail().isBlank()) {
                return ApiResponse.error("Primary emergency contact email missing");
            }

            String locationLink =
                    "https://www.google.com/maps/search/?api=1&query="
                            + request.getLatitude() + "," + request.getLongitude();

            String messageBody =
                    "🚨 SOS ALERT 🚨\n\n" +
                            "User: " + userEmail + "\n" +
                            "Live Location: " + locationLink + "\n\n" +
                            "Please help immediately.";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(primaryContact.getEmail());
            message.setSubject("🚨 SOS Alert");
            message.setText(messageBody);

            mailSender.send(message);

            return ApiResponse.success(
                    "SOS alert sent to " + primaryContact.getName()
            );
        }
    }
