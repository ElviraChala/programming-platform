package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.StudentConverter;
import com.elvira.programming_platform.dto.StudentDTO;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;

    public StudentService(StudentRepository studentRepository, StudentConverter studentConverter) {
        this.studentRepository = studentRepository;
        this.studentConverter = studentConverter;
    }

    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student studentModel = studentConverter.toModel(studentDTO);
        Student savedStudent = studentRepository.save(studentModel);
        return studentConverter.toDTO(savedStudent);
    }

    public StudentDTO readStudentById(Long userId) {
        Student findStudent = studentRepository.findById(userId).orElse(null);
        if (findStudent == null) {
            return null;
        }
        return studentConverter.toDTO(findStudent);
    }

    public List<StudentDTO> readAllStudents() {
        List<Student> findStudents = (List<Student>) studentRepository.findAll();
        return findStudents.stream()
                .map(studentConverter::toDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO updateStudent(StudentDTO newStudentDTO) {
        Long userId = newStudentDTO.getId();
        Student findStudent = studentRepository.findById(userId).orElse(null);
        if (findStudent == null) {
            return null;
        }

        Student studentModel = studentConverter.toModel(newStudentDTO);
        Student updatedStudent = studentRepository.save(studentModel);
        return studentConverter.toDTO(updatedStudent);
    }

    public void deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        }
    }
}
