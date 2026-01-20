package practice.demo.service.users;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.user.*;
import practice.demo.entity.EmergencyContact;
import practice.demo.entity.User;
import practice.demo.entity.UserProfile;
import practice.demo.repository.EmergencyContactRepository;
import practice.demo.repository.UserProfileRepository;
import practice.demo.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRegisterService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final EmergencyContactRepository emergencyContactRepository;

    // ===============================
    // Register user profile with emergency contacts
    // ===============================
    public ApiResponse registerProfile(Long userId, RegisterProfileRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userProfileRepository.findByUserId(userId) != null) {
            return ApiResponse.error("User profile already exists");
        }

        // Save user profile
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setFullName(request.getFullName());
        profile.setMobile(request.getMobile());
        profile.setGender(request.getGender());
        profile.setAddressLine1(request.getAddressLine1());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setPincode(request.getPincode());
        profile.setCountry(request.getCountry());

        profile = userProfileRepository.save(profile);

        // Validate emergency contacts
        if (request.getEmergencyContacts() == null || request.getEmergencyContacts().isEmpty()) {
            return ApiResponse.error("At least one emergency contact is required");
        }

        // Add emergency contacts (all as primary)
        for (EmergencyContactRequest ecReq : request.getEmergencyContacts()) {
            EmergencyContact contact = new EmergencyContact();
            contact.setUserProfile(profile);
            contact.setName(ecReq.getName());
            contact.setPhone(ecReq.getPhone());
            contact.setEmail(ecReq.getEmail());
            contact.setIsPrimary(true); // all contacts default primary

            emergencyContactRepository.save(contact);
        }

        return ApiResponse.success("User registered successfully", buildResponse(profile));
    }

    // ===============================
    // Add new emergency contact later
    // ===============================
    public ApiResponse addEmergencyContact(Long userId, AddEmergencyContactRequest request) {

        UserProfile profile = userProfileRepository.findByUserId(userId);
        if (profile == null) {
            return ApiResponse.error("User profile not found");
        }

        // Fetch existing contacts
        List<EmergencyContact> existingContacts = emergencyContactRepository.findByUserProfileId(profile.getId());

        // Check for duplicates
        boolean isDuplicate = existingContacts.stream().anyMatch(ec ->
                ec.getPhone().equals(request.getPhone()) ||
                        (ec.getEmail() != null && ec.getEmail().equalsIgnoreCase(request.getEmail()))
        );

        if (isDuplicate) {
            return ApiResponse.error("This emergency contact already exists");
        }

        // New contact is automatically primary
        EmergencyContact contact = new EmergencyContact();
        contact.setUserProfile(profile);
        contact.setName(request.getName());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        contact.setIsPrimary(true); // new contact always primary

        emergencyContactRepository.save(contact);

        return ApiResponse.success("Emergency contact added successfully");
    }

    // ===============================
    // Build response for profile
    // ===============================
    private UserProfileResponse buildResponse(UserProfile profile) {

        List<EmergencyContactResponse> contacts =
                emergencyContactRepository.findByUserProfileId(profile.getId())
                        .stream()
                        .map(ec -> new EmergencyContactResponse(
                                ec.getName(),
                                ec.getPhone(),
                                ec.getEmail(),
                                ec.getIsPrimary()
                        ))
                        .collect(Collectors.toList());

        UserProfileResponse res = new UserProfileResponse();
        res.setFullName(profile.getFullName());
        res.setMobile(profile.getMobile());
        res.setGender(profile.getGender());
        res.setAddressLine1(profile.getAddressLine1());
        res.setCity(profile.getCity());
        res.setState(profile.getState());
        res.setPincode(profile.getPincode());
        res.setCountry(profile.getCountry());
        res.setEmergencyContacts(contacts);

        return res;
    }


    public ApiResponse changeEmergencyContactPrimaryStatus(
            Long userId,
            Long contactId,
            Boolean newStatus
    ) {

        if (newStatus == null) {
            return ApiResponse.error("isPrimary must be true or false");
        }

        UserProfile profile = userProfileRepository.findByUserId(userId);
        if (profile == null) {
            return ApiResponse.error("User profile not found");
        }

        EmergencyContact contact = emergencyContactRepository.findById(contactId)
                .orElse(null);

        if (contact == null ||
                !contact.getUserProfile().getId().equals(profile.getId())) {
            return ApiResponse.error("Emergency contact not found");
        }

        // 🔐 LAST PRIMARY PROTECTION
        if (Boolean.FALSE.equals(newStatus)) {

            long primaryCount =
                    emergencyContactRepository
                            .countByUserProfileIdAndIsPrimaryTrue(profile.getId());

            if (primaryCount <= 1 && Boolean.TRUE.equals(contact.getIsPrimary())) {
                return ApiResponse.error(
                        "At least one emergency contact must remain primary"
                );
            }
        }

        contact.setIsPrimary(newStatus);
        emergencyContactRepository.save(contact);

        return ApiResponse.success("Emergency contact primary status updated");
    }


}
