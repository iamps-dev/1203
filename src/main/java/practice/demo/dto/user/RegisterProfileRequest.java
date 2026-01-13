package practice.demo.dto.user;

import java.util.List;

public class RegisterProfileRequest {

    private String fullName;
    private String mobile;
    private String gender;

    private String addressLine1;
    private String city;
    private String state;
    private String pincode;
    private String country;

    private List<EmergencyContactRequest> emergencyContacts;

    // GETTERS & SETTERS
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public List<EmergencyContactRequest> getEmergencyContacts() {
        return emergencyContacts;
    }
    public void setEmergencyContacts(List<EmergencyContactRequest> emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
    }
}
