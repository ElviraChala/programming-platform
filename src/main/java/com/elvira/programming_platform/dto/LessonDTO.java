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

    @Override
    public String toString() {
        return "LessonDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", orderIndex=" + orderIndex +
                ", courseId=" + courseId +
                ", theory=" + theory +
                ", checkKnowledgeId=" + checkKnowledgeId +
                ", programmingTaskIds=" + programmingTaskIds +
                '}';
    }
}
