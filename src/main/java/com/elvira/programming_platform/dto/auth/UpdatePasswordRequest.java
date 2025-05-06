package com.elvira.programming_platform.dto.auth;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    String email;
    String password;
}
