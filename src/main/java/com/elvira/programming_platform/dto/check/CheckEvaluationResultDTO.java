package com.elvira.programming_platform.dto.check;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckEvaluationResultDTO {
    private double totalScore;
    private double lowScore;
    private double mediumScore;
    private double highScore;
}
