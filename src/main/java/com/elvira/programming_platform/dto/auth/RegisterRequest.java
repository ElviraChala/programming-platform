package com.elvira.programming_platform.dto.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String name;
    private String password;
    private String email;
}
