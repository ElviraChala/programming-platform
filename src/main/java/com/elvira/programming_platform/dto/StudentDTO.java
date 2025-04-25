package com.elvira.programming_platform.dto;

import lombok.Data;

import java.util.Set;

@Data
public class StudentDTO extends UserDTO {
    private int score;
    private Set<Long> coursesId;
    private Boolean isFirst;
}
