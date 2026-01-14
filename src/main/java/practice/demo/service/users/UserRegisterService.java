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
public class    UserRegisterService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final EmergencyContactRepository emergencyContactRepository;

    public ApiResponse registerProfile(Long userId, RegisterProfileRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userProfileRepository.findByUserId(userId) != null) {
            return ApiResponse.error("User profile already exists");
        }

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

        profile = userProfileRepository.save(profile); // ✅ Save and get proper ID

        if (request.getEmergencyContacts() == null || request.getEmergencyContacts().isEmpty()) {
            return ApiResponse.error("At least one emergency contact is required");
        }

        long primaryCount = request.getEmergencyContacts().stream()
                .filter(c -> Boolean.TRUE.equals(c.getIsPrimary()))
                .count();

        if (primaryCount != 1) {
            return ApiResponse.error("Exactly one emergency contact must be primary");
        }

        for (EmergencyContactRequest ecReq : request.getEmergencyContacts()) {

            EmergencyContact contact = new EmergencyContact();
            contact.setUserProfile(profile); // ✅ Proper profile with correct ID

            contact.setName(ecReq.getName());
            contact.setPhone(ecReq.getPhone());
            contact.setEmail(ecReq.getEmail());
            contact.setIsPrimary(ecReq.getIsPrimary());

            emergencyContactRepository.save(contact);
        }

        return ApiResponse.success("User registered successfully", buildResponse(profile));
    }

    private UserProfileResponse buildResponse(UserProfile profile) {

        List<EmergencyContactResponse> contacts =
                emergencyContactRepository  .findByUserProfileId(profile.getId())
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
}
