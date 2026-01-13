package practice.demo.dto.user;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class UserProfileResponse {

    private String fullName;
    private String mobile;
    private String gender;
    private String addressLine1;
    private String city;
    private String state;
    private String pincode;
    private String country;

    private List<EmergencyContactResponse> emergencyContacts;
}
