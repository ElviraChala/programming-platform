package com.elvira.programming_platform.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CourseDTO {
    private Long id;
    private String name;
    private Set<Long> studentsIds = new HashSet<>();
    private Set<Long> coursesIds = new HashSet<>();
}
