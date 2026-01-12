package practice.demo.dto.user;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class RegisterProfileRequest {

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

    // List of maps for emergency contacts: name, phone, email
    private List<Map<String, Object>> emergencyContacts;

    // ✅ No-args constructor (important for Jackson)
    public RegisterProfileRequest() {}

    // ✅ Getters
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

    // ✅ Setters
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setGender(String gender) { this.gender = gender; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public void setCountry(String country) { this.country = country; }
    public void setEmergencyContacts(List<Map<String, Object>> emergencyContacts) { this.emergencyContacts = emergencyContacts; }
}
