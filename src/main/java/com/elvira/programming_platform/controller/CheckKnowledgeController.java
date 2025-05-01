package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.QuestionDTO;
import com.elvira.programming_platform.dto.check.AnswerDTO;
import com.elvira.programming_platform.dto.check.CheckKnowledgeDTO;
import com.elvira.programming_platform.service.check.CheckKnowledgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/check-knowledge")
public class CheckKnowledgeController {

    private final CheckKnowledgeService checkKnowledgeService;

    public CheckKnowledgeController(CheckKnowledgeService checkKnowledgeService) {
        this.checkKnowledgeService = checkKnowledgeService;
    }

    @PostMapping
    public ResponseEntity<CheckKnowledgeDTO> createCheckKnowledge(@RequestBody CheckKnowledgeDTO checkKnowledgeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checkKnowledgeService.createCheckKnowledge(checkKnowledgeDTO));
    }

    @GetMapping
    public ResponseEntity<CheckKnowledgeDTO> readCheckKnowledgeById(@RequestParam Long id) {
        CheckKnowledgeDTO dto = checkKnowledgeService.readCheckKnowledgeById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/questions")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByCheckKnowledgeId(@PathVariable Long id) {
        List<QuestionDTO> questions = checkKnowledgeService.getQuestionsByCheckKnowledgeId(id);
        log.info(String.valueOf(questions));
        return questions != null ? ResponseEntity.ok(questions) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Double> checkKnowledge(@PathVariable Long id, @RequestBody List<AnswerDTO> answers) {
        Double result = checkKnowledgeService.evaluateAnswers(id, answers);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<CheckKnowledgeDTO> updateCheckKnowledge(@RequestBody CheckKnowledgeDTO checkKnowledgeDTO) {
        CheckKnowledgeDTO dto = checkKnowledgeService.updateCheckKnowledge(checkKnowledgeDTO);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheckKnowledge(@PathVariable Long id) {
        checkKnowledgeService.deleteCheckKnowledge(id);
        return ResponseEntity.ok().build();
    }
}

