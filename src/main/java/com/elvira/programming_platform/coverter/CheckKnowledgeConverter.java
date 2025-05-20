package com.elvira.programming_platform.coverter;

import com.elvira.programming_platform.dto.check.CheckKnowledgeDTO;
import com.elvira.programming_platform.dto.QuestionDTO;
import com.elvira.programming_platform.model.CheckKnowledge;
import com.elvira.programming_platform.model.Question;
import com.elvira.programming_platform.repository.LessonRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CheckKnowledgeConverter {

    private final LessonRepository lessonRepository;
    private final QuestionConverter questionConverter;

    public CheckKnowledgeConverter(LessonRepository lessonRepository, QuestionConverter questionConverter) {
        this.lessonRepository = lessonRepository;
        this.questionConverter = questionConverter;
    }

    public CheckKnowledge toModel(CheckKnowledgeDTO source) {
        CheckKnowledge target = new CheckKnowledge();
        target.setId(source.getId());
        target.setTestWeight(source.getTestWeight());

        if (source.getQuestions() != null) {
            List<Question> questions = source.getQuestions()
                    .stream()
                    .map(dto -> {
                        Question question = questionConverter.toModel(dto);
                        question.setCheckKnowledge(target);
                        return question;
                    })
                    .toList();

            target.setQuestions(questions);
        }

        if (source.getLessonId() != null) {
            target.setLesson(lessonRepository.findById(source.getLessonId()).orElse(null));
        }

        return target;
    }

    public CheckKnowledgeDTO toDTO(CheckKnowledge source) {
        CheckKnowledgeDTO target = new CheckKnowledgeDTO();
        target.setId(source.getId());
        target.setTestWeight(source.getTestWeight());
        if (source.getQuestions() != null) {
            List<QuestionDTO> questionDTOs = source.getQuestions()
                    .stream()
                    .map(questionConverter::toDTO)
                    .toList();
            target.setQuestions(questionDTOs);
        }

        if (source.getLesson() != null) {
            target.setLessonId(source.getLesson().getId());
        }

        return target;
    }
}
