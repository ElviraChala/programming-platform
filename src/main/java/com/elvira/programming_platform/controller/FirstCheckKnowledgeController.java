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
}
