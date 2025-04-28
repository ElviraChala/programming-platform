package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.FirstCheckKnowledgeDTO;
import com.elvira.programming_platform.service.FirstCheckKnowledgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/first-check")
@RequiredArgsConstructor
public class FirstCheckKnowledgeController {

    private final FirstCheckKnowledgeService firstCheckKnowledgeService;

    @GetMapping("")
    public ResponseEntity<FirstCheckKnowledgeDTO> getLastCheck() {
        return ResponseEntity.ok(firstCheckKnowledgeService.getFirstCheckKnowledge());
    }
}
