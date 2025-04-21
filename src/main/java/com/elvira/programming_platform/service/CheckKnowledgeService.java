package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.CheckKnowledgeConverter;
import com.elvira.programming_platform.dto.CheckKnowledgeDTO;
import com.elvira.programming_platform.model.CheckKnowledge;
import com.elvira.programming_platform.repository.CheckKnowledgeRepository;
import org.springframework.stereotype.Service;

@Service
public class CheckKnowledgeService {

    private final CheckKnowledgeRepository checkKnowledgeRepository;
    private final CheckKnowledgeConverter checkKnowledgeConverter;

    public CheckKnowledgeService(CheckKnowledgeRepository checkKnowledgeRepository, CheckKnowledgeConverter checkKnowledgeConverter) {
        this.checkKnowledgeRepository = checkKnowledgeRepository;
        this.checkKnowledgeConverter = checkKnowledgeConverter;
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
}
