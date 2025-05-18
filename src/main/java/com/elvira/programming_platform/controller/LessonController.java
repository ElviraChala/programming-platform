package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.LessonDTO;
import com.elvira.programming_platform.service.LessonService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    /**
     * Uploads an HTML file for a lesson's theory.
     * 
     * @param file The HTML file to upload
     * @param fileName The name to save the file as
     * @return The file name that was saved
     */
    @PostMapping("/upload-html")
    public ResponseEntity<String> uploadHtmlFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName) {
        try {
            String savedFileName = lessonService.uploadHtmlFile(file, fileName);
            return ResponseEntity.ok(savedFileName);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

    /**
     * Downloads an HTML file for a lesson's theory.
     * 
     * @param fileName The name of the file to download
     * @return The file as a Resource
     */
    @GetMapping("/download-html/{fileName}")
    public ResponseEntity<Resource> downloadHtmlFile(@PathVariable String fileName) {
        try {
            Resource resource = lessonService.downloadHtmlFile(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
