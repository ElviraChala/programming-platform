package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.auth.AuthResponse;
import com.elvira.programming_platform.dto.auth.EmailRequest;
import com.elvira.programming_platform.dto.auth.LoginRequest;
import com.elvira.programming_platform.dto.auth.RegisterRequest;
import com.elvira.programming_platform.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody EmailRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok().build();
    }
}
