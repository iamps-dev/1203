package practice.demo.controller.user;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.web.bind.annotation.*;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.user.SOSRequest;
import practice.demo.service.users.SOSService;

@RestController
@RequestMapping("/api/auth/user")
@RequiredArgsConstructor
public class SOSController {

    private final SOSService sosService;

    @PostMapping("/sos")
    public ApiResponse sendSOS(
            Authentication authentication,
            @RequestBody SOSRequest request
    ) {
        String email = authentication.name(); // from JWT
        return sosService.sendSOS(email, request);
    }
}
