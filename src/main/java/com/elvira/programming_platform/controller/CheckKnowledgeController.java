package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.CheckKnowledgeDTO;
import com.elvira.programming_platform.service.CheckKnowledgeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("check-knowledge")
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

