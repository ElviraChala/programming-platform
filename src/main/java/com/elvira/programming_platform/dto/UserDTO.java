package com.elvira.programming_platform.dto;

import com.elvira.programming_platform.model.Role;
import lombok.Data;

@Data
public class UserDTO {
    protected Long id;
    protected String username;
    protected String name;
    protected String password;
    protected String email;
    protected Role role;
}
