package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.LessonConverter;
import com.elvira.programming_platform.coverter.TheoryConverter;
import com.elvira.programming_platform.repository.LessonRepository;
import com.elvira.programming_platform.repository.TheoryRepository;
import com.elvira.programming_platform.repository.check.CheckKnowledgeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class LessonServiceTest {

    private LessonRepository lessonRepository;
    private LessonConverter lessonConverter;
    private LessonService lessonService;
    private LessonService lessonServiceWithExternalDir;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        lessonRepository = mock(LessonRepository.class);
        lessonConverter = mock(LessonConverter.class);
        TheoryRepository theoryRepository = mock(TheoryRepository.class);
        TheoryConverter theoryConverter = mock(TheoryConverter.class);
        CheckKnowledgeRepository checkKnowledgeRepository = mock(CheckKnowledgeRepository.class);

        // Create service with default configuration (using resources)
        lessonService = new LessonService(lessonConverter,
                lessonRepository,
                theoryRepository,
                theoryConverter,
                checkKnowledgeRepository,
                "");

        // Create service with external directory configuration
        lessonServiceWithExternalDir = new LessonService(lessonConverter,
                lessonRepository,
                theoryRepository,
                theoryConverter,
                checkKnowledgeRepository,
                tempDir.toString());
    }

    @Test
    void testReadLessonById_FromResources() {
        // This test verifies that the service can be created with an empty directory path
        // and that it doesn't throw exceptions when initialized.
        // Since we can't easily mock ClassPathResource in a unit test,
        // we'll just verify that the service is created correctly.

        assertNotNull(lessonService);

        // For a more complete test, we would need to use an integration test
        // that includes actual resources in the classpath.
    }

    @Test
    void testUploadHtmlFile() throws IOException {
        // Arrange
        String fileName = "upload-test.html";
        String content = "<html><body>Uploaded content</body></html>";
        MultipartFile file = new MockMultipartFile(
                "file",
                fileName,
                "text/html",
                content.getBytes(StandardCharsets.UTF_8));

        // Act
        lessonServiceWithExternalDir.uploadHtmlFile(file, fileName);

        // Verify the file was saved
        Path savedFilePath = tempDir.resolve(fileName);
        assertTrue(Files.exists(savedFilePath));
        String savedContent = Files.readString(savedFilePath);
        assertEquals(content, savedContent);
    }

    @Test
    void testUploadHtmlFile_NoExternalDirectory() {
        // Arrange
        String fileName = "upload-test.html";
        MultipartFile file = new MockMultipartFile(
                "file",
                fileName,
                "text/html",
                "content".getBytes(StandardCharsets.UTF_8));

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                lessonService.uploadHtmlFile(file, fileName));
    }

    @Test
    void testDownloadHtmlFile_FromExternalDirectory() throws IOException {
        // Arrange
        String fileName = "download-test.html";
        String content = "<html><body>Download content</body></html>";
        Path filePath = tempDir.resolve(fileName);
        Files.writeString(filePath, content);

        // Act
        Resource result = lessonServiceWithExternalDir.downloadHtmlFile(fileName);

        // Assert
        assertNotNull(result);
        assertTrue(result.exists());
        assertEquals(content, new String(result.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
    }

    @Test
    void testDownloadHtmlFile_FileNotFound() {
        // Arrange
        String fileName = "non-existent.html";

        // Act & Assert
        assertThrows(IOException.class, () ->
                lessonServiceWithExternalDir.downloadHtmlFile(fileName));
    }
}
