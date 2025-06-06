package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.CourseDTO;
import com.elvira.programming_platform.security.JwtService;
import com.elvira.programming_platform.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    private final JwtService jwtService;

    public CourseController(CourseService courseService, JwtService jwtService) {
        this.courseService = courseService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(courseService.createCourse(courseDTO));
    }

    @GetMapping
    public ResponseEntity<CourseDTO> readCourseById(@RequestParam Long id) {
        return ResponseEntity.ok(courseService.readCourseById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseDTO>> readAllCourses(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(courseService.readAllCoursesByStudentLevel(username));
    }

    @PutMapping
    public ResponseEntity<CourseDTO> updateCourse(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(courseService.updateCourse(courseDTO));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCourse(@RequestParam Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
