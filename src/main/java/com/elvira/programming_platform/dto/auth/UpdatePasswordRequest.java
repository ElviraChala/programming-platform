package com.elvira.programming_platform.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePasswordRequest {
    String email;
    String password;
}
