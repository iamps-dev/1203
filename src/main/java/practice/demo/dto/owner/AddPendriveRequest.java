package practice.demo.dto.owner;

import lombok.Data;

@Data
public class AddPendriveRequest {

    private String serialNumber;   // REQUIRED
    private String labelName;      // MANUAL NAME
}
