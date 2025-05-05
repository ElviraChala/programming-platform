package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.TheoryConverter;
import com.elvira.programming_platform.dto.TheoryDTO;
import com.elvira.programming_platform.model.Theory;
import com.elvira.programming_platform.repository.TheoryRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class TheoryService {
    private final TheoryConverter theoryConverter;
    private final TheoryRepository theoryRepository;

    public TheoryService(TheoryConverter theoryConverter, TheoryRepository theoryRepository) {
        this.theoryConverter = theoryConverter;
        this.theoryRepository = theoryRepository;
    }

    public TheoryDTO createTheory(TheoryDTO theoryDTO) {
        Theory theoryModel = theoryConverter.toModel(theoryDTO);
        Theory savedTheory = theoryRepository.save(theoryModel);
        return theoryConverter.toDTO(savedTheory);
    }

    public TheoryDTO readTheoryById(Long userId) {
        Theory findTheory = theoryRepository.findById(userId).orElse(null);
        if (findTheory == null) {
            return null;
        }
        return theoryConverter.toDTO(findTheory);
    }

    public List<TheoryDTO> readAllTheories() {
        List<Theory> findTheories = (List<Theory>) theoryRepository.findAll();
        return findTheories.stream()
                .map(theoryConverter::toDTO)
                .toList();
    }

    public TheoryDTO updateTheory(TheoryDTO newTheoryDTO) {
        Long userId = newTheoryDTO.getId();
        Theory findTheory = theoryRepository.findById(userId).orElse(null);
        if (findTheory == null) {
            return null;
        }

        Theory theoryModel = theoryConverter.toModel(newTheoryDTO);
        Theory updatedTheory = theoryRepository.save(theoryModel);
        return theoryConverter.toDTO(updatedTheory);
    }

    public void deleteTheory(Long id) {
        if (theoryRepository.existsById(id)) {
            theoryRepository.deleteById(id);
        }
    }
}
