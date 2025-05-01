package com.elvira.programming_platform.coverter;

import com.elvira.programming_platform.dto.QuestionDTO;
import com.elvira.programming_platform.model.Question;
import com.elvira.programming_platform.repository.check.CheckKnowledgeRepository;
import org.springframework.stereotype.Component;

@Component
public class QuestionConverter {

    private final CheckKnowledgeRepository checkKnowledgeRepository;

    public QuestionConverter(CheckKnowledgeRepository checkKnowledgeRepository) {
        this.checkKnowledgeRepository = checkKnowledgeRepository;
    }

    public Question toModel(QuestionDTO source) {
        Question target = new Question();
        target.setId(source.getId());
        target.setText(source.getText());

        if (source.getOptions() != null) {
            target.setOptions(source.getOptions());
        }

        target.setCorrectAnswer(source.getCorrectAnswer());
        target.setCheckKnowledge(checkKnowledgeRepository.findById(source.getCheckKnowledgeId())
                .orElse(null));

        target.setLevel(source.getLevel());

        return target;
    }

    public QuestionDTO toDTO(Question source) {
        QuestionDTO target = new QuestionDTO();
        target.setId(source.getId());
        target.setText(source.getText());

        if (source.getOptions() != null) {
            target.setOptions(source.getOptions());
        }

        target.setCorrectAnswer(source.getCorrectAnswer());
        if (source.getCheckKnowledge() != null) {

            target.setCheckKnowledgeId(source.getCheckKnowledge().getId());
        }
        target.setLevel(source.getLevel());

        return target;
    }
}
