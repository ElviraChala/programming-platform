package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.auth.*;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.security.JwtService;
import com.elvira.programming_platform.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegister_Success() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setName("Test User");
        request.setEmail("test@example.com");

        AuthResponse response = new AuthResponse("jwt-token", 1L);

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        AuthResponse response = new AuthResponse("jwt-token", 1L);

        when(authService.authenticate(any(LoginRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testForgotPassword_Success() throws Exception {
        // Arrange
        EmailRequest request = new EmailRequest();
        request.setEmail("test@example.com");

        // Act & Assert
        mockMvc.perform(post("/api/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePassword_Success() throws Exception {
        // Arrange
        UpdatePasswordRequest request = new UpdatePasswordRequest("test@example.com", "newpassword");
        String token = "Bearer jwt-token";

        when(jwtService.extractUsername("jwt-token")).thenReturn("testuser");

        // Act & Assert
        mockMvc.perform(post("/api/auth/update")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePassword_InvalidToken() throws Exception {
        // Arrange
        UpdatePasswordRequest request = new UpdatePasswordRequest("test@example.com", "newpassword");
        String token = "Bearer invalid-token";

        when(jwtService.extractUsername("invalid-token")).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/auth/update")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCheckUsername_Found() throws Exception {
        // Arrange
        String username = "testuser";
        Student student = new Student();
        student.setEmail("test@example.com");

        when(authService.findByUsername(username)).thenReturn(Optional.of(student));

        // Act & Assert
        mockMvc.perform(get("/api/auth/check-username")
                .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testCheckUsername_NotFound() throws Exception {
        // Arrange
        String username = "nonexistentuser";

        when(authService.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/auth/check-username")
                .param("username", username))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCheckUsername_NoEmail() throws Exception {
        // Arrange
        String username = "testuser";
        Student student = new Student();
        student.setEmail(null);

        when(authService.findByUsername(username)).thenReturn(Optional.of(student));

        // Act & Assert
        mockMvc.perform(get("/api/auth/check-username")
                .param("username", username))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value(""));
    }

    @Test
    void testVerifyEmail_Success() throws Exception {
        // Arrange
        String token = "valid-token";

        when(authService.verifyEmail(token)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/auth/verify-email")
                .param("token", token))
                .andExpect(status().isOk());
    }

    @Test
    void testVerifyEmail_Failure() throws Exception {
        // Arrange
        String token = "invalid-token";

        when(authService.verifyEmail(token)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/auth/verify-email")
                .param("token", token))
                .andExpect(status().isBadRequest());
    }
}