package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.FirstCheckKnowledgeConverter;
import com.elvira.programming_platform.dto.FirstCheckKnowledgeDTO;
import com.elvira.programming_platform.model.FirstCheckKnowledge;
import com.elvira.programming_platform.repository.FirstCheckKnowledgeRepository;
import org.springframework.stereotype.Service;

@Service
public class FirstCheckKnowledgeService {

    private final FirstCheckKnowledgeRepository firstCheckKnowledgeRepository;
    private final FirstCheckKnowledgeConverter firstCheckKnowledgeConverter;

    public FirstCheckKnowledgeService(FirstCheckKnowledgeRepository firstCheckKnowledgeRepository,
                                      FirstCheckKnowledgeConverter firstCheckKnowledgeConverter) {
        this.firstCheckKnowledgeRepository = firstCheckKnowledgeRepository;
        this.firstCheckKnowledgeConverter = firstCheckKnowledgeConverter;
    }

    public FirstCheckKnowledgeDTO getFirstCheckKnowledge() {
        FirstCheckKnowledge check = firstCheckKnowledgeRepository.findTopByOrderByIdDesc().orElseThrow();
        return firstCheckKnowledgeConverter.toDTO(check);
    }
}
