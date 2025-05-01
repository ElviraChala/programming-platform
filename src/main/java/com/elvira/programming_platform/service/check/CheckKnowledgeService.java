package com.elvira.programming_platform.service.check;

import com.elvira.programming_platform.coverter.CheckKnowledgeConverter;
import com.elvira.programming_platform.coverter.QuestionConverter;
import com.elvira.programming_platform.dto.QuestionDTO;
import com.elvira.programming_platform.dto.check.AnswerDTO;
import com.elvira.programming_platform.dto.check.CheckKnowledgeDTO;
import com.elvira.programming_platform.model.CheckKnowledge;
import com.elvira.programming_platform.model.Question;
import com.elvira.programming_platform.repository.check.CheckKnowledgeRepository;
import java.util.Collections;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckKnowledgeService {

    private final CheckKnowledgeRepository checkKnowledgeRepository;
    private final CheckKnowledgeConverter checkKnowledgeConverter;
    private final QuestionConverter questionConverter;

    public CheckKnowledgeService(CheckKnowledgeRepository checkKnowledgeRepository,
                                 CheckKnowledgeConverter checkKnowledgeConverter, QuestionConverter questionConverter) {
        this.checkKnowledgeRepository = checkKnowledgeRepository;
        this.checkKnowledgeConverter = checkKnowledgeConverter;
        this.questionConverter = questionConverter;
    }

    public CheckKnowledgeDTO createCheckKnowledge (CheckKnowledgeDTO checkKnowledgeDTO){
        CheckKnowledge checkKnowledgeModel = checkKnowledgeConverter.toModel(checkKnowledgeDTO);
        CheckKnowledge savedCheckKnowledge = checkKnowledgeRepository.save(checkKnowledgeModel);
        return checkKnowledgeConverter.toDTO(savedCheckKnowledge);
    }

    public CheckKnowledgeDTO readCheckKnowledgeById(Long checkKnowledgeId){
        CheckKnowledge findCheckKnowledge = checkKnowledgeRepository.findById(checkKnowledgeId).orElse(null);
        if (findCheckKnowledge == null){
            return null;
        }
        return checkKnowledgeConverter.toDTO(findCheckKnowledge);
    }

    public CheckKnowledgeDTO updateCheckKnowledge(CheckKnowledgeDTO checkKnowledgeDTO){
        Long id = checkKnowledgeDTO.getId();
        CheckKnowledge findCheckKnowledge = checkKnowledgeRepository.findById(id).orElse(null);
        if(findCheckKnowledge == null){
            return null;
        }

        findCheckKnowledge.getQuestions().clear();
        findCheckKnowledge.getQuestions().addAll(
                checkKnowledgeConverter.toModel(checkKnowledgeDTO).getQuestions()
        );

        CheckKnowledge updated = checkKnowledgeRepository.save(findCheckKnowledge);
        return checkKnowledgeConverter.toDTO(updated);
    }

    public void deleteCheckKnowledge(Long id){
        if(checkKnowledgeRepository.existsById(id)){
            checkKnowledgeRepository.deleteById(id);
        }
    }

    public List<QuestionDTO> getQuestionsByCheckKnowledgeId(Long id) {
        CheckKnowledge checkKnowledge = checkKnowledgeRepository.findById(id).orElse(null);
        if (checkKnowledge == null) return Collections.emptyList();

        return checkKnowledge.getQuestions().stream()
                .map(questionConverter::toDTO)
                .toList();
    }


    public double evaluateAnswers(Long id, List<AnswerDTO> answers) {
        CheckKnowledge check = checkKnowledgeRepository.findById(id).orElse(null);
        if (check == null) return 0.;

        int correctCount = 0;
        int total = check.getQuestions().size();

        List<Question> questionIds = check.getQuestions();
        for(int i = 0; i < answers.size(); i++){
            if(answers.get(i).getCurrentAnswer().equals(questionIds.get(i).getCorrectAnswer())){
                correctCount++;
            }
        }
        return (double)correctCount * 100 / total;
    }
}
