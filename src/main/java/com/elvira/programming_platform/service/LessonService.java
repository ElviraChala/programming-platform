package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.LessonConverter;
import com.elvira.programming_platform.dto.LessonDTO;
import com.elvira.programming_platform.dto.TheoryDTO;
import com.elvira.programming_platform.model.Lesson;
import com.elvira.programming_platform.repository.LessonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    public LessonService(
            LessonConverter lessonConverter, 
            LessonRepository lessonRepository,
            @Value("${theory.html.directory:}") String htmlDirectoryPath) {
        this.lessonConverter = lessonConverter;
        this.lessonRepository = lessonRepository;
        this.htmlDirectoryPath = htmlDirectoryPath;
        this.useExternalDirectory = htmlDirectoryPath != null && !htmlDirectoryPath.isEmpty();
    }

    public LessonDTO createLesson(LessonDTO lessonDTO) {
        Lesson lessonModel = lessonConverter.toModel(lessonDTO);
        Lesson savedLesson = lessonRepository.save(lessonModel);
        return lessonConverter.toDTO(savedLesson);
    }

    public LessonDTO readLessonById(Long id) {
        Lesson findLesson = lessonRepository.findById(id).orElseThrow();
        LessonDTO lessonDTO = lessonConverter.toDTO(findLesson);
        TheoryDTO theoryDTO = lessonDTO.getTheory();

        String htmlFilePath;
        if (useExternalDirectory) {
            // When using external directory, the path is relative to the directory
            htmlFilePath = theoryDTO.getFileName();
        } else {
            // When using resources, the path includes the static/html/theory prefix
            htmlFilePath = "/static/html/theory/" + theoryDTO.getFileName();
        }

        theoryDTO.setFileName(getHtml(htmlFilePath));
        return lessonDTO;
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
     * @return The file name that was saved
     * @throws IOException If there was an error saving the file
     */
    public String uploadHtmlFile(MultipartFile file, String fileName) throws IOException {
        if (!useExternalDirectory) {
            throw new IllegalStateException("Cannot upload files when external directory is not configured");
        }

        Path targetLocation = Paths.get(htmlDirectoryPath, fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        log.info("Saved HTML file to: {}", targetLocation);
        return fileName;
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

    private String getHtml(String htmlPath) {
        Resource resource;
        if (useExternalDirectory) {
            // Load from external directory
            Path fullPath = Paths.get(htmlDirectoryPath, htmlPath);
            resource = new FileSystemResource(fullPath);
            log.info("Loading HTML from external directory: {}", fullPath);
        } else {
            // Load from resources directory
            resource = new ClassPathResource(htmlPath);
            log.info("Loading HTML from resources: {}", htmlPath);
        }

        try {
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to load HTML file: {}", htmlPath, e);
            throw new RuntimeException("Failed to load HTML file: " + htmlPath, e);
        }
    }
}
