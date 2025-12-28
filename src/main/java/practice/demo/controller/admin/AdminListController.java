package practice.demo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import practice.demo.dto.admin.AdminResponse;
import practice.demo.service.admin.AdminListService;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminListController {

    @Autowired
    private AdminListService adminListService;

    // ✅ GET all admins
    @GetMapping("/all")
    public List<AdminResponse> getAllAdmins() {

        return adminListService.getAllAdmins();

    }
}
