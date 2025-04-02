package com.elvira.programming_platform.coverter;

import com.elvira.programming_platform.dto.AdminDTO;
import com.elvira.programming_platform.model.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminConverter {
    public Admin toModel(AdminDTO source) {
        Admin target = new Admin();
        target.setId(source.getId());
        target.setEmail(source.getEmail());
        target.setUsername(source.getUsername());
        target.setName(source.getName());
        target.setPassword(source.getPassword());
        target.setEmail(source.getEmail());
        target.setRole(source.getRole());
        return target;
    }

    public AdminDTO toDTO(Admin source) {
        AdminDTO target = new AdminDTO();
        target.setId(source.getId());
        target.setEmail(source.getEmail());
        target.setUsername(source.getUsername());
        target.setName(source.getName());
        target.setPassword(source.getPassword());
        target.setEmail(source.getEmail());
        target.setRole(source.getRole());
        return target;
    }
}
