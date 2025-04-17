package com.elvira.programming_platform.coverter;

import com.elvira.programming_platform.dto.QuestionDTO;
import com.elvira.programming_platform.model.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionConverter {

    public Question toModel(QuestionDTO source){
        Question target = new Question();

        return target;
    }

    public QuestionDTO toDTO(Question source){
        QuestionDTO target = new QuestionDTO();

        return target;
    }
}
