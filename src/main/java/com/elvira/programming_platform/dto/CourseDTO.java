package com.elvira.programming_platform.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CourseDTO {
    private Long id;
    private String name;
    private String description;
    private Set<Long> studentIds = new HashSet<>();
    private Set<Long> lessonIds = new HashSet<>();
}
