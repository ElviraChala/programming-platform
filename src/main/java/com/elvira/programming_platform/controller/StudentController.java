package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.StudentDTO;
import com.elvira.programming_platform.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO){
        return ResponseEntity.ok(studentService.createStudent(studentDTO));
    }

    @GetMapping
    public ResponseEntity<StudentDTO> readStudentById(@RequestParam Long id){
        return ResponseEntity.ok(studentService.readStudentById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<StudentDTO>> readAllStudents(){
        return ResponseEntity.ok(studentService.readAllStudents());
    }

    @PutMapping
    public ResponseEntity<StudentDTO> updateStudent(@RequestBody StudentDTO studentDTO){
        return ResponseEntity.ok(studentService.updateStudent(studentDTO));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStudent(@RequestParam Long id){
        studentService.deleteStudent(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

