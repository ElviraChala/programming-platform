package com.elvira.programming_platform.dto;

import lombok.Data;

import java.util.Set;

@Data
public class LessonDTO {
    private Long id;
    private String name;
    private int orderIndex;
    private Long courseId;
    private TheoryDTO theory;
    private Long checkKnowledgeId;
    private Set<Long> programmingTaskIds;
}
