package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.AdminDTO;
import com.elvira.programming_platform.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<AdminDTO> createAdmin(@RequestBody AdminDTO adminDTO){
        return ResponseEntity.ok(adminService.createAdmin(adminDTO));
    }

    @GetMapping
    public ResponseEntity<AdminDTO> readAdminById(@RequestParam Long id){
        return ResponseEntity.ok(adminService.readAdminById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdminDTO>> readAllAdmins(){
        return ResponseEntity.ok(adminService.readAllAdmins());
    }

    @PutMapping
    public ResponseEntity<AdminDTO> updateAdmin(@RequestBody AdminDTO adminDTO){
        return ResponseEntity.ok(adminService.updateAdmin(adminDTO));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAdmin(@RequestParam Long id){
        adminService.deleteAdmin(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
