package com.elvira.programming_platform.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String text;
    private List<String> options;
    private String correctAnswer;
    private Long checkKnowledgeId;
}
