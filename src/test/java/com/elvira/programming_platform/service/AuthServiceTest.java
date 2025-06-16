package com.elvira.programming_platform.service;

import com.elvira.programming_platform.dto.auth.*;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.model.VerificationToken;
import com.elvira.programming_platform.repository.StudentRepository;
import com.elvira.programming_platform.repository.VerificationTokenRepository;
import com.elvira.programming_platform.security.JwtService;
import com.elvira.programming_platform.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private StudentRepository studentRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private EmailService emailService;
    private VerificationTokenRepository verificationTokenRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtService = mock(JwtService.class);
        authenticationManager = mock(AuthenticationManager.class);
        emailService = mock(EmailService.class);
        verificationTokenRepository = mock(VerificationTokenRepository.class);

        authService = new AuthService(
                studentRepository,
                passwordEncoder,
                jwtService,
                authenticationManager,
                emailService,
                verificationTokenRepository
        );
    }

    @Test
    void testRegister_Success() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setName("Test User");
        request.setEmail("test@example.com");

        when(studentRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        
        Student savedStudent = new Student();
        savedStudent.setId(1L);
        savedStudent.setUsername("testuser");
        savedStudent.setPassword("encodedPassword");
        savedStudent.setName("Test User");
        savedStudent.setEmail("test@example.com");
        
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);
        when(jwtService.generateToken(any(UserDetailsImpl.class))).thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(1L, response.getId());
        
        verify(studentRepository).save(any(Student.class));
        verify(verificationTokenRepository).save(any(VerificationToken.class));
        verify(emailService).sendSimpleEmail(eq("test@example.com"), anyString(), anyString());
        verify(jwtService).generateToken(any(UserDetailsImpl.class));
    }

    @Test
    void testRegister_UsernameAlreadyTaken() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");
        request.setPassword("password");

        Student existingStudent = new Student();
        existingStudent.setUsername("existinguser");
        
        when(studentRepository.findByUsername("existinguser")).thenReturn(Optional.of(existingStudent));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testAuthenticate_Success() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        Student student = new Student();
        student.setId(1L);
        student.setUsername("testuser");
        
        when(studentRepository.findByUsername("testuser")).thenReturn(Optional.of(student));
        when(jwtService.generateToken(any(UserDetailsImpl.class))).thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(1L, response.getId());
        
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("testuser", "password")
        );
    }

    @Test
    void testAuthenticate_UserNotFound() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistentuser");
        request.setPassword("password");

        when(studentRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());
        when(authenticationManager.authenticate(any())).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.authenticate(request));
    }

    @Test
    void testForgotPassword_Success() {
        // Arrange
        EmailRequest request = new EmailRequest();
        request.setEmail("test@example.com");

        Student student = new Student();
        student.setEmail("test@example.com");
        
        when(studentRepository.findByEmail("test@example.com")).thenReturn(Optional.of(student));
        when(jwtService.generateToken(any(UserDetailsImpl.class))).thenReturn("jwt-token");

        // Act
        authService.forgotPassword(request);

        // Assert
        verify(emailService).sendSimpleEmail(eq("test@example.com"), eq("New password"), contains("jwt-token"));
    }

    @Test
    void testForgotPassword_EmailNotFound() {
        // Arrange
        EmailRequest request = new EmailRequest();
        request.setEmail("nonexistent@example.com");

        when(studentRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.forgotPassword(request));
        verify(emailService, never()).sendSimpleEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testUpdatePassword_Success() {
        // Arrange
        UpdatePasswordRequest request = new UpdatePasswordRequest("updated@example.com", "newpassword");

        Student student = new Student();
        student.setEmail("old@example.com");
        student.setPassword("oldpassword");
        
        when(studentRepository.findByUsername("testuser")).thenReturn(Optional.of(student));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");

        // Act
        authService.updatePassword("testuser", request);

        // Assert
        assertEquals("updated@example.com", student.getEmail());
        assertEquals("encodedNewPassword", student.getPassword());
        verify(studentRepository).save(student);
    }

    @Test
    void testUpdatePassword_StudentNotFound() {
        // Arrange
        UpdatePasswordRequest request = new UpdatePasswordRequest("email@example.com", "password");
        
        when(studentRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.updatePassword("nonexistentuser", request));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testVerifyEmail_Success() {
        // Arrange
        String token = "valid-token";
        
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        verificationToken.setVerified(false);
        
        Student student = new Student();
        verificationToken.setUser(student);
        
        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        // Act
        boolean result = authService.verifyEmail(token);

        // Assert
        assertTrue(result);
        assertTrue(student.isEmailVerified());
        assertTrue(verificationToken.isVerified());
        verify(studentRepository).save(student);
        verify(verificationTokenRepository).save(verificationToken);
    }

    @Test
    void testVerifyEmail_TokenNotFound() {
        // Arrange
        String token = "invalid-token";
        
        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // Act
        boolean result = authService.verifyEmail(token);

        // Assert
        assertFalse(result);
        verify(studentRepository, never()).save(any(Student.class));
        verify(verificationTokenRepository, never()).save(any(VerificationToken.class));
    }

    @Test
    void testVerifyEmail_TokenExpired() {
        // Arrange
        String token = "expired-token";
        
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(LocalDateTime.now().minusHours(1));
        
        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        // Act
        boolean result = authService.verifyEmail(token);

        // Assert
        assertFalse(result);
        verify(studentRepository, never()).save(any(Student.class));
        verify(verificationTokenRepository, never()).save(any(VerificationToken.class));
    }

    @Test
    void testVerifyEmail_AlreadyVerified() {
        // Arrange
        String token = "already-verified-token";
        
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        verificationToken.setVerified(true);
        
        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        // Act
        boolean result = authService.verifyEmail(token);

        // Assert
        assertFalse(result);
        verify(studentRepository, never()).save(any(Student.class));
        verify(verificationTokenRepository, never()).save(any(VerificationToken.class));
    }
}