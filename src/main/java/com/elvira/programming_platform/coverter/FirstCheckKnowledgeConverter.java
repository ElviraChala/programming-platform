package com.elvira.programming_platform.coverter;

import com.elvira.programming_platform.dto.FirstCheckKnowledgeDTO;
import com.elvira.programming_platform.model.FirstCheckKnowledge;
import com.elvira.programming_platform.model.Question;
import com.elvira.programming_platform.repository.QuestionRepository;
import org.springframework.stereotype.Component;

@Component
public class FirstCheckKnowledgeConverter {

    private final QuestionRepository questionRepository;

    public FirstCheckKnowledgeConverter(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public FirstCheckKnowledge toModel(FirstCheckKnowledgeDTO source) {
        FirstCheckKnowledge target = new FirstCheckKnowledge();
        target.setId(source.getId());
        target.setQuestions(source.getQuestionIds().stream()
                .map(id -> questionRepository.findById(id).orElseThrow())
                .toList());

        return target;
    }

    public FirstCheckKnowledgeDTO toDTO(FirstCheckKnowledge source) {
        FirstCheckKnowledgeDTO target = new FirstCheckKnowledgeDTO();
        target.setId(source.getId());
        target.setQuestionIds(source.getQuestions().stream()
                .map(Question::getId)
                .toList());

        return target;
    }
}
