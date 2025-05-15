package com.elvira.programming_platform.dto.check;

import lombok.Data;

@Data
public class InterpreterResponse {
    private Boolean isOk;
    private String expectedOutput;
    private String actualOutput;
}
