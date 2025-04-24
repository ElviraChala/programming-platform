package com.elvira.programming_platform.service;

import com.elvira.programming_platform.dto.auth.AuthResponse;
import com.elvira.programming_platform.dto.auth.LoginRequest;
import com.elvira.programming_platform.dto.auth.RegisterRequest;
import com.elvira.programming_platform.model.Role;
import com.elvira.programming_platform.model.User;
import com.elvira.programming_platform.repository.UserRepository;
import com.elvira.programming_platform.security.JwtService;
import com.elvira.programming_platform.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .build();
        repository.save(user);
        String jwt = jwtService.generateToken(new UserDetailsImpl(user));
        return new AuthResponse(jwt);
    }

    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(new UserDetailsImpl(user));
        return new AuthResponse(jwt);
    }
}
