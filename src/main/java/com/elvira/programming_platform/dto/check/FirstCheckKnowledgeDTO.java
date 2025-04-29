package com.elvira.programming_platform.dto.check;

import lombok.Data;

import java.util.List;

@Data
public class FirstCheckKnowledgeDTO {
    private Long id;
    private List<Long> questionIds;
}
