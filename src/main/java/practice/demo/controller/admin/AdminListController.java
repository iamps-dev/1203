package practice.demo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import practice.demo.dto.admin.AdminListResponse;
import practice.demo.service.admin.AdminListService;

@RestController
@RequestMapping("/api/auth/admin")
public class AdminListController {

    @Autowired
    private AdminListService adminListService;

    // ✅ ONLY SUPER_ADMIN CAN ACCESS
    @GetMapping("/all")
    public AdminListResponse getAllAdmins(
            @RequestHeader("Authorization") String authorizationHeader) {

        return adminListService.getAllAdmins(authorizationHeader);
    }
}
