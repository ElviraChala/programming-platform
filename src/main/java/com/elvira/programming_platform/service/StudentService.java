package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.StudentConverter;
import com.elvira.programming_platform.dto.StudentDTO;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                          StudentConverter studentConverter,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.studentConverter = studentConverter;
        this.passwordEncoder = passwordEncoder;
    }

    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student studentModel = studentConverter.toModel(studentDTO);
        studentModel.setPassword(passwordEncoder.encode(studentModel.getPassword()));
        Student savedStudent = studentRepository.save(studentModel);
        return studentConverter.toDTO(savedStudent);
    }

    public StudentDTO readStudentById(Long userId) {
        Student findStudent = studentRepository.findById(userId).orElseThrow();
        return studentConverter.toDTO(findStudent);
    }

    public List<StudentDTO> readAllStudents() {
        List<Student> findStudents = (List<Student>) studentRepository.findAll();
        return findStudents.stream()
                .map(studentConverter::toDTO)
                .toList();
    }

    public StudentDTO updateStudent(StudentDTO newStudentDTO) {
        Long userId = newStudentDTO.getId();
        Student existingStudent = studentRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + userId));

        if (newStudentDTO.getPassword() != null && !newStudentDTO.getPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(newStudentDTO.getPassword());
            existingStudent.setPassword(encodedPassword);
        }

        Student updatedStudent = studentRepository.save(existingStudent);
        return studentConverter.toDTO(updatedStudent);
    }

    public void deleteStudent(String username) {
        if (studentRepository.existsByUsername(username)) {
            studentRepository.deleteByUsername(username);
        }
    }

    public StudentDTO readStudentByName(String username) {
        Student student = studentRepository.findByUsername(username).orElseThrow();
        return studentConverter.toDTO(student);
    }
}
