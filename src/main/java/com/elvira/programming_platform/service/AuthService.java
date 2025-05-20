package com.elvira.programming_platform.service;

import com.elvira.programming_platform.dto.auth.*;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.model.User;
import com.elvira.programming_platform.model.VerificationToken;
import com.elvira.programming_platform.repository.StudentRepository;
import com.elvira.programming_platform.repository.VerificationTokenRepository;
import com.elvira.programming_platform.security.JwtService;
import com.elvira.programming_platform.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;

    public AuthResponse register(RegisterRequest request) {
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }
        Student student = new Student();
        student.setUsername(request.getUsername());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student = repository.save(student);

        // Create verification token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(student);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        verificationTokenRepository.save(verificationToken);

        // Send verification email
        String verificationUrl = "http://dev-spark.fun/api/auth/verify-email?token=" + token;
        String emailText = "Будь ласка, підтвердіть свою електронну адресу, перейшовши за посиланням:\n" + verificationUrl;
        emailService.sendSimpleEmail(student.getEmail(), "Підтвердження електронної адреси", emailText);

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
        Optional<Student> student = repository.findByEmail(email);
        if (student.isEmpty()) {
            throw new IllegalArgumentException("Email does not exist");
        }

        String text = "Перейдіть по посиланню для створення нового паролю\n"
                + "http://dev-spark.fun/auth/create-new-password?email=" + email + "&token=" + jwtService.generateToken(new UserDetailsImpl(student.get())) + "\n\n";
        emailService.sendSimpleEmail(email, "New password", text);
    }

    public void updatePassword(String userName, UpdatePasswordRequest request) {
        Optional<Student> student = repository.findByUsername(userName);
        if (student.isEmpty()) {
            throw new IllegalArgumentException("Student does not exist");
        }

        Student existingStudent = student.get();
        existingStudent.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(request.getPassword().trim());
            existingStudent.setPassword(encodedPassword);
        }
        repository.save(existingStudent);
    }

    public Optional<Student> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public boolean verifyEmail(String token) {
        Optional<VerificationToken> verificationTokenOpt = verificationTokenRepository.findByToken(token);
        if (verificationTokenOpt.isEmpty()) {
            return false;
        }

        VerificationToken verificationToken = verificationTokenOpt.get();
        if (verificationToken.isExpired() || verificationToken.isVerified()) {
            return false;
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);

        // Since our repository only handles Student entities, we need to check if the user is a Student
        if (user instanceof Student student) {
            repository.save(student);
        }

        verificationToken.setVerified(true);
        verificationTokenRepository.save(verificationToken);

        return true;
    }
}
