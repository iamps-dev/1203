package practice.demo.dto.user;

import lombok.Data;
import java.util.List;

@Data
public class SOSRequest {

    private double latitude;
    private double longitude;

    // ✅ Selected emergency contact IDs
    private List<Long> contactIds;
}
