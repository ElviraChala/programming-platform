package com.elvira.programming_platform.dto;

import com.elvira.programming_platform.model.CheckKnowledge;
import com.elvira.programming_platform.model.Course;
import com.elvira.programming_platform.model.ProgrammingTask;
import com.elvira.programming_platform.model.Theory;
import lombok.Data;

@Data
public class LessonDTO {
    private Long id;
    private String name;
    private int orderIndex;
    private Course course;
    private Theory theory;
    private CheckKnowledge checkKnowledge;
    private ProgrammingTask programmingTask;
}
