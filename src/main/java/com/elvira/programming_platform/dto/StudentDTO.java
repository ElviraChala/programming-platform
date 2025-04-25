package com.elvira.programming_platform.dto;

import com.elvira.programming_platform.model.enums.Level;
import lombok.Data;

import java.util.Set;

@Data
public class StudentDTO extends UserDTO {
    private int score;
    private Set<Long> coursesId;
    private Boolean isFirst;
    private Level level = Level.LOW;
}
