package practice.demo.dto.user;   // ✅ MUST BE HERE

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class UserProfileResponse {

    private String fullName;
    private String mobile;
    private String gender;
    private LocalDate dateOfBirth;
    private String bloodGroup;

    private String addressLine1;
    private String city;
    private String state;
    private String pincode;
    private String country;

    private List<Map<String, Object>> emergencyContacts;

    // ✅ No-args constructor (recommended for Jackson)
    public UserProfileResponse() {}

    public UserProfileResponse(
            String fullName,
            String mobile,
            String gender,
            LocalDate dateOfBirth,
            String bloodGroup,
            String addressLine1,
            String city,
            String state,
            String pincode,
            String country,
            List<Map<String, Object>> emergencyContacts
    ) {
        this.fullName = fullName;
        this.mobile = mobile;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.bloodGroup = bloodGroup;
        this.addressLine1 = addressLine1;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.country = country;
        this.emergencyContacts = emergencyContacts;
    }

    // ✅ ALL GETTERS (important)
    public String getFullName() { return fullName; }
    public String getMobile() { return mobile; }
    public String getGender() { return gender; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getBloodGroup() { return bloodGroup; }
    public String getAddressLine1() { return addressLine1; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getPincode() { return pincode; }
    public String getCountry() { return country; }
    public List<Map<String, Object>> getEmergencyContacts() { return emergencyContacts; }
}
