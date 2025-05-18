package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.StudentConverter;
import com.elvira.programming_platform.dto.StudentDTO;
import com.elvira.programming_platform.model.CheckKnowledge;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.repository.StudentRepository;
import com.elvira.programming_platform.repository.check.CheckKnowledgeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

class StudentServiceTest {

    private StudentRepository studentRepository;
    private StudentConverter studentConverter;
    private PasswordEncoder passwordEncoder;
    private CheckKnowledgeRepository checkKnowledgeRepository;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        studentConverter = mock(StudentConverter.class);
        passwordEncoder = mock(PasswordEncoder.class);
        checkKnowledgeRepository = mock(CheckKnowledgeRepository.class);
        studentService = new StudentService(studentRepository, studentConverter, passwordEncoder, checkKnowledgeRepository);
    }

    @Test
    void testCreateStudent() {
        StudentDTO dto = new StudentDTO();
        dto.setPassword("plainPass");
        Student model = new Student();
        model.setPassword("plainPass");

        Student encodedModel = new Student();
        encodedModel.setPassword("encodedPass");

        Student saved = new Student();
        StudentDTO savedDto = new StudentDTO();

        when(studentConverter.toModel(dto)).thenReturn(model);
        when(passwordEncoder.encode("plainPass")).thenReturn("encodedPass");
        when(studentRepository.save(any(Student.class))).thenReturn(saved);
        when(studentConverter.toDTO(saved)).thenReturn(savedDto);

        StudentDTO result = studentService.createStudent(dto);

        assertEquals(savedDto, result);
        verify(passwordEncoder).encode("plainPass");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void testReadStudentById_Found() {
        Student student = new Student();
        StudentDTO dto = new StudentDTO();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentConverter.toDTO(student)).thenReturn(dto);

        StudentDTO result = studentService.readStudentById(1L);

        assertEquals(dto, result);
    }

    @Test
    void testReadStudentById_NotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> studentService.readStudentById(1L));
    }

    @Test
    void testReadAllStudents() {
        Student student = new Student();
        StudentDTO dto = new StudentDTO();

        when(studentRepository.findAll()).thenReturn(List.of(student));
        when(studentConverter.toDTO(student)).thenReturn(dto);

        List<StudentDTO> result = studentService.readAllStudents();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void testUpdateStudent_WithPassword() {
        StudentDTO newDTO = new StudentDTO();
        newDTO.setId(1L);
        newDTO.setName("New Name");
        newDTO.setEmail("new@example.com");
        newDTO.setPassword("newpass");

        Student existing = new Student();
        existing.setId(1L);
        existing.setName("Old Name");
        existing.setEmail("old@example.com");

        Student updated = new Student();
        StudentDTO updatedDTO = new StudentDTO();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNewPass");
        when(studentRepository.save(existing)).thenReturn(updated);
        when(studentConverter.toDTO(updated)).thenReturn(updatedDTO);

        StudentDTO result = studentService.updateStudent(newDTO);

        assertEquals(updatedDTO, result);
        verify(passwordEncoder).encode("newpass");
        verify(studentRepository).save(existing);
    }

    @Test
    void testUpdateStudent_WithoutPassword() {
        StudentDTO newDTO = new StudentDTO();
        newDTO.setId(1L);
        newDTO.setName("Name");
        newDTO.setEmail("email@example.com");
        newDTO.setPassword(null); // no password change

        Student existing = new Student();
        StudentDTO updatedDTO = new StudentDTO();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(existing)).thenReturn(existing);
        when(studentConverter.toDTO(existing)).thenReturn(updatedDTO);

        StudentDTO result = studentService.updateStudent(newDTO);

        assertEquals(updatedDTO, result);
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void testUpdateStudent_NotFound() {
        StudentDTO dto = new StudentDTO();
        dto.setId(999L);

        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> studentService.updateStudent(dto));
    }

    @Test
    void testDeleteStudent_Exists() {
        when(studentRepository.existsByUsername("user")).thenReturn(true);

        studentService.deleteStudent("user");

        verify(studentRepository).deleteByUsername("user");
    }

    @Test
    void testDeleteStudent_NotExists() {
        when(studentRepository.existsByUsername("user")).thenReturn(false);

        studentService.deleteStudent("user");

        verify(studentRepository, never()).deleteByUsername(anyString());
    }

    @Test
    void testReadStudentByName_Found() {
        Student student = new Student();
        StudentDTO dto = new StudentDTO();

        when(studentRepository.findByUsername("user")).thenReturn(Optional.of(student));
        when(studentConverter.toDTO(student)).thenReturn(dto);

        StudentDTO result = studentService.readStudentByName("user");

        assertEquals(dto, result);
    }

    @Test
    void testReadStudentByName_NotFound() {
        when(studentRepository.findByUsername("user")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> studentService.readStudentByName("user"));
    }

    @Test
    void testAddScore() {
        // Arrange
        Long studentId = 1L;
        Long checkKnowledgeId = 2L;

        Student student = spy(new Student());
        student.setId(studentId);

        CheckKnowledge checkKnowledge = new CheckKnowledge();
        checkKnowledge.setId(checkKnowledgeId);
        checkKnowledge.setTestWeight(10);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(checkKnowledgeRepository.findById(checkKnowledgeId)).thenReturn(Optional.of(checkKnowledge));

        // Act
        studentService.addScore(studentId, checkKnowledgeId);

        // Assert
        verify(student).addPassedTest(checkKnowledge);
        verify(studentRepository).save(student);
    }
}
