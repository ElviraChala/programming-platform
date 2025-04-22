package com.elvira.programming_platform.service;

import com.elvira.programming_platform.dto.auth.RegisterRequest;
import com.elvira.programming_platform.exception.EmailAlreadyExistsException;
import com.elvira.programming_platform.exception.UsernameAlreadyExistsException;
import com.elvira.programming_platform.model.Role;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {
        validateUsername(request.getUsername());
        validateEmail(request.getEmail());

        Student student = new Student();
        student.setUsername(request.getUsername());
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setRole(Role.STUDENT);
        student.setScore(0);

        studentRepository.save(student);
    }



    private void validateUsername(String username) {
        if (studentRepository.findByUsername(username)) {
            throw new UsernameAlreadyExistsException("Username '" + username + "' already exists");
        }
    }

    private void validateEmail(String email) {
        if (studentRepository.findByEmail(email)) {
            throw new EmailAlreadyExistsException("Email '" + email + "' is already registered");
        }
    }

}
