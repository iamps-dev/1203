package practice.demo.controller.owners;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.owner.AddPendriveRequest;
import practice.demo.service.owners.PendriveAuthService;

@RestController
@RequestMapping("/api/hardware")
@RequiredArgsConstructor
public class PendriveController {

    private final PendriveAuthService pendriveAuthService;

    @PostMapping("/add")
    public ApiResponse addPendrive(@RequestBody AddPendriveRequest request) {
        return pendriveAuthService.addPendrive(request);
    }
}
