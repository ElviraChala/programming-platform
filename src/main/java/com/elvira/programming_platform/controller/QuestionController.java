package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.QuestionDTO;
import com.elvira.programming_platform.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("question")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO) {
        return ResponseEntity.ok(questionService.createQuestion(questionDTO));
    }

    @GetMapping
    public ResponseEntity<QuestionDTO> readQuestionById(@RequestParam Long id) {
        return ResponseEntity.ok(questionService.readQuestionById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<QuestionDTO>> readAllQuestions() {
        return ResponseEntity.ok(questionService.readAllQuestions());
    }

    @PutMapping
    public ResponseEntity<QuestionDTO> updateQuestion(@RequestBody QuestionDTO questionDTO) {
        QuestionDTO updated = questionService.updateQuestion(questionDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteQuestion(@RequestParam Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
