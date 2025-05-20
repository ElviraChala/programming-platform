package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.LessonConverter;
import com.elvira.programming_platform.coverter.TheoryConverter;
import com.elvira.programming_platform.dto.LessonDTO;
import com.elvira.programming_platform.dto.TheoryDTO;
import com.elvira.programming_platform.model.CheckKnowledge;
import com.elvira.programming_platform.model.Lesson;
import com.elvira.programming_platform.model.Theory;
import com.elvira.programming_platform.repository.LessonRepository;
import com.elvira.programming_platform.repository.TheoryRepository;
import com.elvira.programming_platform.repository.check.CheckKnowledgeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Slf4j
@Service
public class LessonService {
    private final LessonConverter lessonConverter;
    private final LessonRepository lessonRepository;
    private final String htmlDirectoryPath;
    private final boolean useExternalDirectory;
    private final TheoryRepository theoryRepository;
    private final TheoryConverter theoryConverter;
    private final CheckKnowledgeRepository checkKnowledgeRepository;

    public LessonService(
            LessonConverter lessonConverter,
            LessonRepository lessonRepository,
            TheoryRepository theoryRepository,
            TheoryConverter theoryConverter,
            CheckKnowledgeRepository checkKnowledgeRepository,
            @Value("${theory.html.directory:}") String htmlDirectoryPath) {
        this.lessonConverter = lessonConverter;
        this.lessonRepository = lessonRepository;
        this.htmlDirectoryPath = htmlDirectoryPath;
        this.useExternalDirectory = htmlDirectoryPath != null && !htmlDirectoryPath.isEmpty();
        this.theoryRepository = theoryRepository;
        this.theoryConverter = theoryConverter;
        this.checkKnowledgeRepository = checkKnowledgeRepository;
    }

    public LessonDTO createLesson(LessonDTO lessonDTO) {
        Lesson lessonModel = lessonConverter.toModel(lessonDTO);

        Lesson savedLesson = lessonRepository.save(lessonModel);

        CheckKnowledge checkKnowledge = new CheckKnowledge();
        checkKnowledge.setLesson(savedLesson);
        checkKnowledge.setTestWeight(1);

        lessonModel.setCheckKnowledge(checkKnowledgeRepository.save(checkKnowledge));
        savedLesson = lessonRepository.save(lessonModel);

        TheoryDTO theory = lessonDTO.getTheory();
        if (theory != null) {
            theory.setLessonId(savedLesson.getId());
            theoryRepository.save(theoryConverter.toModel(theory));
        }
        savedLesson = lessonRepository.findById(savedLesson.getId()).orElseThrow();
        return lessonConverter.toDTO(savedLesson);
    }

    public LessonDTO readLessonById(Long id) {
        Lesson findLesson = lessonRepository.findById(id).orElseThrow();
        Theory theory = findLesson.getTheory();

        if (theory != null) {
            theory.setFileName(theory.getFileName());
        }

        CheckKnowledge checkKnowledge = findLesson.getCheckKnowledge();
        if (checkKnowledge == null) {
            checkKnowledge = new CheckKnowledge();
            checkKnowledge.setLesson(findLesson);
            checkKnowledge.setTestWeight(1);
            checkKnowledge = checkKnowledgeRepository.save(checkKnowledge);

            findLesson.setCheckKnowledge(checkKnowledge);
            findLesson = lessonRepository.save(findLesson);
        }
        return lessonConverter.toDTO(findLesson);
    }

    public List<LessonDTO> readAllLessons() {
        List<Lesson> findLessons = (List<Lesson>) lessonRepository.findAll();
        return findLessons.stream()
                .map(lessonConverter::toDTO)
                .toList();
    }

    public LessonDTO updateLesson(LessonDTO newLessonDTO){
        Long lessonId = newLessonDTO.getId();
        Lesson findLesson = lessonRepository.findById(lessonId).orElse(null);
        if (findLesson == null) return null;

        Lesson lessonModel = lessonConverter.toModel(newLessonDTO);
        Lesson updatedLesson = lessonRepository.save(lessonModel);
        return lessonConverter.toDTO(updatedLesson);
    }

    public void deleteLesson(Long id) {
        if (lessonRepository.existsById(id)) {
            lessonRepository.deleteById(id);
        }
    }

    /**
     * Uploads an HTML file to the configured directory.
     * 
     * @param file The HTML file to upload
     * @param fileName The name to save the file as
     * @throws IOException If there was an error saving the file
     */
    public void uploadHtmlFile(MultipartFile file, String fileName) throws IOException {
        if (!useExternalDirectory) {
            throw new IllegalStateException("Cannot upload files when external directory is not configured");
        }

        Path targetLocation = Paths.get(htmlDirectoryPath, fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        log.info("Saved HTML file to: {}", targetLocation);
    }

    /**
     * Gets an HTML file as a Resource for downloading.
     * 
     * @param fileName The name of the file to download
     * @return The file as a Resource
     * @throws IOException If there was an error reading the file
     */
    public Resource downloadHtmlFile(String fileName) throws IOException {
        Resource resource;
        if (useExternalDirectory) {
            Path filePath = Paths.get(htmlDirectoryPath, fileName);
            resource = new FileSystemResource(filePath);
            log.info("Downloading HTML from external directory: {}", filePath);
        } else {
            String resourcePath = "/static/html/theory/" + fileName;
            resource = new ClassPathResource(resourcePath);
            log.info("Downloading HTML from resources: {}", resourcePath);
        }

        if (!resource.exists()) {
            throw new IOException("File not found: " + fileName);
        }

        return resource;
    }
}
