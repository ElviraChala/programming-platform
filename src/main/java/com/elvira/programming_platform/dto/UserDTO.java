package com.elvira.programming_platform.dto;

import com.elvira.programming_platform.model.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String password;
    private String email;
    private Role role;
}
