package com.elvira.programming_platform.dto;

import com.elvira.programming_platform.model.CheckKnowledge;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String text;
    private List<String> options;
    private String correctAnswer;
    private CheckKnowledge checkKnowledge;
}
