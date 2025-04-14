package com.elvira.programming_platform.dto;

import com.elvira.programming_platform.model.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CheckKnowledgeDTO {
    private Long id;
    private List<Question> questions = new ArrayList<>();

}
