package practice.demo.service.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.user.*;
import practice.demo.entity.User;
import practice.demo.entity.UserProfile;
import practice.demo.repository.UserProfileRepository;
import practice.demo.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApiResponse registerProfile(String email, RegisterProfileRequest request) throws JsonProcessingException {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null)
            return ApiResponse.error("User not found");

        if (userProfileRepository.findByUserId(user.getId()).isPresent())
            return ApiResponse.error("Profile already completed");

        if (request.getEmergencyContacts() == null || request.getEmergencyContacts().isEmpty())
            return ApiResponse.error("At least one emergency contact required");

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setFullName(request.getFullName());
        profile.setMobile(request.getMobile());
        profile.setGender(request.getGender());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setBloodGroup(request.getBloodGroup());
        profile.setAddressLine1(request.getAddressLine1());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setPincode(request.getPincode());
        profile.setCountry(request.getCountry());

        // store JSON in DB
        String json = new ObjectMapper().writeValueAsString(request.getEmergencyContacts());
        profile.setEmergencyContacts(json);

        userProfileRepository.save(profile);

        UserProfileResponse response = new UserProfileResponse(
                profile.getFullName(),
                profile.getMobile(),
                profile.getGender(),
                profile.getDateOfBirth(),
                profile.getBloodGroup(),
                profile.getAddressLine1(),
                profile.getCity(),
                profile.getState(),
                profile.getPincode(),
                profile.getCountry(),
                request.getEmergencyContacts()
        );

        return ApiResponse.success("User registered successfully", response);
    }
}
