package com.elvira.programming_platform.dto;

import lombok.Data;

@Data
public class LessonDTO {
    private Long id;
    private String name;
    private int orderIndex;
    private CourseDTO course;
    private TheoryDTO theory;
    private CheckKnowledgeDTO checkKnowledge;
    private ProgrammingTaskDTO programmingTask;
}
