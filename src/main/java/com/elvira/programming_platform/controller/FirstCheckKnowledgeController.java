package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.check.CheckEvaluationResultDTO;
import com.elvira.programming_platform.dto.check.FirstCheckKnowledgeDTO;
import com.elvira.programming_platform.dto.check.AnswerDTO;
import com.elvira.programming_platform.dto.check.CheckResultDTO;
import com.elvira.programming_platform.model.enums.Level;
import com.elvira.programming_platform.security.JwtService;
import com.elvira.programming_platform.service.check.FirstCheckKnowledgeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/first-check")
@RequiredArgsConstructor
public class FirstCheckKnowledgeController {

    private final FirstCheckKnowledgeService firstCheckKnowledgeService;
    private final JwtService jwtService;


    @GetMapping("")
    public ResponseEntity<FirstCheckKnowledgeDTO> getLastCheck() {
        return ResponseEntity.ok(firstCheckKnowledgeService.getFirstCheckKnowledge());
    }

    @PostMapping("")
    public ResponseEntity<CheckResultDTO> saveFirstCheck(@RequestBody List<AnswerDTO> answers,
                                                         @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);

        CheckEvaluationResultDTO checkResult = firstCheckKnowledgeService.checkAnswer(answers);
        Level level;
        try {
            level = firstCheckKnowledgeService.setLevel(username,
                    checkResult.getLowScore(),
                    checkResult.getMediumScore(),
                    checkResult.getHighScore());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }

        CheckResultDTO result = new CheckResultDTO(checkResult.getTotalScore(), level);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create")
    public ResponseEntity<FirstCheckKnowledgeDTO> createFirstCheckKnowledge(@RequestBody FirstCheckKnowledgeDTO dto) {
        try {
            FirstCheckKnowledgeDTO createdDto = firstCheckKnowledgeService.createFirstCheckKnowledge(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FirstCheckKnowledgeDTO> updateFirstCheckKnowledge(@PathVariable Long id, 
                                                                           @RequestBody FirstCheckKnowledgeDTO dto) {
        try {
            FirstCheckKnowledgeDTO updatedDto = firstCheckKnowledgeService.updateFirstCheckKnowledge(id, dto);
            return ResponseEntity.ok(updatedDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
