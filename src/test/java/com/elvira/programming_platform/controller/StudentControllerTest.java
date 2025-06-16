package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.StudentDTO;
import com.elvira.programming_platform.model.enums.Role;
import com.elvira.programming_platform.security.JwtService;
import com.elvira.programming_platform.service.StudentService;
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

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateStudent_Success() throws Exception {
        // Arrange
        StudentDTO inputDto = new StudentDTO();
        inputDto.setUsername("testuser");
        inputDto.setPassword("password");
        inputDto.setName("Test User");
        inputDto.setEmail("test@example.com");

        StudentDTO outputDto = new StudentDTO();
        outputDto.setId(1L);
        outputDto.setUsername("testuser");
        outputDto.setName("Test User");
        outputDto.setEmail("test@example.com");

        when(studentService.createStudent(any(StudentDTO.class))).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testReadStudent_Success() throws Exception {
        // Arrange
        String token = "Bearer jwt-token";
        String username = "testuser";

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setUsername(username);
        studentDTO.setName("Test User");
        studentDTO.setEmail("test@example.com");

        when(jwtService.extractUsername("jwt-token")).thenReturn(username);
        when(studentService.readStudentByName(username)).thenReturn(studentDTO);

        // Act & Assert
        mockMvc.perform(get("/student")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testReadStudent_InvalidToken() throws Exception {
        // Arrange
        String token = "Bearer invalid-token";

        when(jwtService.extractUsername("invalid-token")).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/student")
                .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @Test
    void testReadAllStudents_Success() throws Exception {
        // Arrange
        StudentDTO student1 = new StudentDTO();
        student1.setId(1L);
        student1.setUsername("user1");
        student1.setName("User One");

        StudentDTO student2 = new StudentDTO();
        student2.setId(2L);
        student2.setUsername("user2");
        student2.setName("User Two");

        List<StudentDTO> students = Arrays.asList(student1, student2);

        when(studentService.readAllStudents()).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/student/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].name").value("User One"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("user2"))
                .andExpect(jsonPath("$[1].name").value("User Two"));
    }

    @Test
    void testUpdateStudent_Success() throws Exception {
        // Arrange
        StudentDTO inputDto = new StudentDTO();
        inputDto.setId(1L);
        inputDto.setUsername("testuser");
        inputDto.setName("Updated Name");
        inputDto.setEmail("updated@example.com");

        StudentDTO outputDto = new StudentDTO();
        outputDto.setId(1L);
        outputDto.setUsername("testuser");
        outputDto.setName("Updated Name");
        outputDto.setEmail("updated@example.com");

        when(studentService.updateStudent(any(StudentDTO.class))).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(put("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void testDeleteStudent_AsAdmin_Success() throws Exception {
        // Arrange
        String token = "Bearer jwt-token";
        String username = "admin";
        Long studentId = 1L;

        StudentDTO adminDTO = new StudentDTO();
        adminDTO.setUsername(username);
        adminDTO.setRole(Role.ADMIN);

        when(jwtService.extractUsername("jwt-token")).thenReturn(username);
        when(studentService.readStudentByName(username)).thenReturn(adminDTO);

        // Act & Assert
        mockMvc.perform(delete("/student")
                .header("Authorization", token)
                .param("id", studentId.toString()))
                .andExpect(status().isOk());

        verify(studentService).deleteStudent(studentId);
    }

    @Test
    void testDeleteStudent_AsStudent_NoDelete() throws Exception {
        // Arrange
        String token = "Bearer jwt-token";
        String username = "student";
        long studentId = 1L;

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setUsername(username);
        studentDTO.setRole(Role.STUDENT);

        when(jwtService.extractUsername("jwt-token")).thenReturn(username);
        when(studentService.readStudentByName(username)).thenReturn(studentDTO);

        // Act & Assert
        mockMvc.perform(delete("/student")
                .header("Authorization", token)
                .param("id", Long.toString(studentId)))
                .andExpect(status().isOk());

        verify(studentService, never()).deleteStudent(anyLong());
    }

    @Test
    void testAddScore_Success() throws Exception {
        // Arrange
        Long studentId = 1L;
        Long checkKnowledgeId = 2L;

        // Act & Assert
        mockMvc.perform(post("/student/{id}/add-score", studentId)
                .param("checkId", checkKnowledgeId.toString()))
                .andExpect(status().isOk());

        verify(studentService).addScore(studentId, checkKnowledgeId);
    }

    @Test
    void testGetLeaderboard_Success() throws Exception {
        // Arrange
        StudentDTO student1 = new StudentDTO();
        student1.setId(1L);
        student1.setUsername("user1");
        student1.setScore(100);

        StudentDTO student2 = new StudentDTO();
        student2.setId(2L);
        student2.setUsername("user2");
        student2.setScore(80);

        List<StudentDTO> students = Arrays.asList(student1, student2);

        when(studentService.getAllSortedByScore()).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/student/all/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].score").value(100))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("user2"))
                .andExpect(jsonPath("$[1].score").value(80));
    }

    @Test
    void testGetStudentById_Success() throws Exception {
        // Arrange
        Long studentId = 1L;

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(studentId);
        studentDTO.setUsername("testuser");
        studentDTO.setName("Test User");
        studentDTO.setEmail("test@example.com");

        when(studentService.readStudentById(studentId)).thenReturn(studentDTO);

        // Act & Assert
        mockMvc.perform(get("/student/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetStudentById_NotFound() throws Exception {
        // Arrange
        Long studentId = 999L;

        when(studentService.readStudentById(studentId)).thenThrow(new NoSuchElementException());

        // Act & Assert
        mockMvc.perform(get("/student/{id}", studentId))
                .andExpect(status().isNotFound());
    }
}