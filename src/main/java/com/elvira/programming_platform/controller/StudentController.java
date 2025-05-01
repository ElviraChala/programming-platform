package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.StudentDTO;
import com.elvira.programming_platform.security.JwtService;
import com.elvira.programming_platform.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.createStudent(studentDTO));
    }

    @GetMapping
    public ResponseEntity<StudentDTO> readStudent(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);
        if (username != null && !username.isEmpty()) {
            StudentDTO student = studentService.readStudentByName(username);
            return ResponseEntity.ok(student);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<StudentDTO>> readAllStudents() {
        return ResponseEntity.ok(studentService.readAllStudents());
    }

    @PutMapping
    public ResponseEntity<StudentDTO> updateStudent(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.updateStudent(studentDTO));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStudent(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);
        studentService.deleteStudent(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}