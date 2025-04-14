package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.AdminConverter;
import com.elvira.programming_platform.dto.AdminDTO;
import com.elvira.programming_platform.model.Admin;
import com.elvira.programming_platform.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminConverter adminConverter;

    public AdminService(AdminRepository adminRepository, AdminConverter adminConverter) {
        this.adminRepository = adminRepository;
        this.adminConverter = adminConverter;
    }

    public AdminDTO createAdmin(AdminDTO adminDTO) {
        Admin adminModel = adminConverter.toModel(adminDTO);
        Admin savedAdmin = adminRepository.save(adminModel);
        return adminConverter.toDTO(savedAdmin);
    }

    public AdminDTO readAdminById(Long userId) {
        Admin findAdmin = adminRepository.findById(userId).orElse(null);
        if (findAdmin == null) {
            return null;
        }
        return adminConverter.toDTO(findAdmin);
    }

    public List<AdminDTO> readAllAdmins() {
        List<Admin> findAdmins = (List<Admin>) adminRepository.findAll();
        return findAdmins.stream()
                .map(adminConverter::toDTO)
                .toList();
    }

    public AdminDTO updateAdmin(AdminDTO newAdminDTO) {
        Long userId = newAdminDTO.getId();
        Admin findAdmin = adminRepository.findById(userId).orElse(null);
        if (findAdmin == null) {
            return null;
        }

        Admin adminModel = adminConverter.toModel(newAdminDTO);
        Admin updatedAdmin = adminRepository.save(adminModel);
        return adminConverter.toDTO(updatedAdmin);
    }

    public void deleteAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
        }
    }
}
