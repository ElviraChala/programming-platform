package com.elvira.programming_platform.service;

import com.elvira.programming_platform.dto.auth.AuthResponse;
import com.elvira.programming_platform.dto.auth.EmailRequest;
import com.elvira.programming_platform.dto.auth.LoginRequest;
import com.elvira.programming_platform.dto.auth.RegisterRequest;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.repository.StudentRepository;
import com.elvira.programming_platform.security.JwtService;
import com.elvira.programming_platform.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthResponse register(RegisterRequest request) {
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }
        Student student = new Student();
        student.setUsername(request.getUsername());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student = repository.save(student);
        String jwt = jwtService.generateToken(new UserDetailsImpl(student));
        return new AuthResponse(jwt, student.getId());
    }

    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        Student student = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + request.getUsername()));
        String jwt = jwtService.generateToken(new UserDetailsImpl(student));
        return new AuthResponse(jwt, student.getId());
    }

    public void forgotPassword(EmailRequest request) {
        String email = request.getEmail();
        if (repository.findByEmail(email).isEmpty()) {
            throw new IllegalArgumentException("Email does not exist");
        }
        emailService.sendSimpleEmail(email, "Test", "Test text");
    }
}
