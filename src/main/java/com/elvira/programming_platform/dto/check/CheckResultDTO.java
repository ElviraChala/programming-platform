package com.elvira.programming_platform.dto.check;

import com.elvira.programming_platform.model.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckResultDTO {
    double score;
    Level level;
}
