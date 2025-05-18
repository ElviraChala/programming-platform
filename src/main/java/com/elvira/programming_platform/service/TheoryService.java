package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.TheoryConverter;
import com.elvira.programming_platform.dto.TheoryDTO;
import com.elvira.programming_platform.model.Theory;
import com.elvira.programming_platform.repository.TheoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class TheoryService {
    private final TheoryConverter theoryConverter;
    private final TheoryRepository theoryRepository;
    private final String htmlDirectoryPath;

    public TheoryService(TheoryConverter theoryConverter,
                         TheoryRepository theoryRepository,
                         @Value("${theory.html.directory:}") String htmlDirectoryPath) {
        this.theoryConverter = theoryConverter;
        this.theoryRepository = theoryRepository;
        this.htmlDirectoryPath = htmlDirectoryPath;
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

    public String readHtml(Long id) {
        Theory theory = theoryRepository.findById(id).orElseThrow();

        String htmlFilePath = theory.getFileName();
        return getHtml(htmlFilePath);
    }

    private String getHtml(String htmlPath) {
        Resource resource;
            Path fullPath = Paths.get(htmlDirectoryPath, htmlPath);
            resource = new FileSystemResource(fullPath);
        try {
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load HTML file: " + htmlPath, e);
        }
    }

}
