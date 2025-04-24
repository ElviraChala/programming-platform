package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.LessonDTO;
import com.elvira.programming_platform.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/lesson")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    public ResponseEntity<LessonDTO> createLesson(@RequestBody LessonDTO lessonDTO) {
        LessonDTO createdLesson = lessonService.createLesson(lessonDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> readLessonById(@PathVariable Long id) {
        LessonDTO dto = lessonService.readLessonById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<LessonDTO>> readAllLessons() {
        return ResponseEntity.ok(lessonService.readAllLessons());
    }

    @PutMapping
    public ResponseEntity<LessonDTO> updateLesson(@RequestBody LessonDTO lessonDTO) {
        LessonDTO updated = lessonService.updateLesson(lessonDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok().build();
    }
}
