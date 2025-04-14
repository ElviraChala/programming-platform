package com.elvira.programming_platform.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CheckKnowledgeDTO {
    private Long id;
    private List<QuestionDTO> questions = new ArrayList<>();
    private Long lessonId;
}
