package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.QuestionConverter;
import com.elvira.programming_platform.dto.QuestionDTO;
import com.elvira.programming_platform.model.Question;
import com.elvira.programming_platform.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private final QuestionConverter questionConverter;
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionConverter questionConverter, QuestionRepository questionRepository) {
        this.questionConverter = questionConverter;
        this.questionRepository = questionRepository;
    }

    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        Question questionModel = questionConverter.toModel(questionDTO);
        Question savedQuestion = questionRepository.save(questionModel);
        return questionConverter.toDTO(savedQuestion);
    }

    public QuestionDTO readQuestionById(Long id) {
        Question findQuestion = questionRepository.findById(id).orElseThrow();
        return questionConverter.toDTO(findQuestion);
    }

    public List<QuestionDTO> readAllQuestions() {
        List<Question> findQuestions = (List<Question>) questionRepository.findAll();
        return findQuestions.stream()
                .map(questionConverter::toDTO)
                .toList();
    }

    public QuestionDTO updateQuestion(QuestionDTO newQuestionDTO) {
        Long questionId = newQuestionDTO.getId();
        Question findQuestion = questionRepository.findById(questionId).orElse(null);
        if (findQuestion == null) return null;

        Question questionModel = questionConverter.toModel(newQuestionDTO);
        Question updatedQuestion = questionRepository.save(questionModel);
        return questionConverter.toDTO(updatedQuestion);
    }

    public void deleteQuestion(Long id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
        }
    }
}
