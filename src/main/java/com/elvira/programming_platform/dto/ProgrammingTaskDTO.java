package com.elvira.programming_platform.dto;

import lombok.Data;

@Data
public class ProgrammingTaskDTO {
    private Long id;
    private String title;
    private String description;
    private String starterCode;
    private String expectedOutput;
    private Long lessonId;
}
