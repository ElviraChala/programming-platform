package com.elvira.programming_platform.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CourseDTO {
    private Long id;
    private String name;
    private Set<Long> studentsId = new HashSet<>();
}
