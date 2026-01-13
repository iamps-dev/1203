package practice.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmergencyContactResponse {

    private String name;
    private String phone;
    private String email;
    private Boolean isPrimary;
}
