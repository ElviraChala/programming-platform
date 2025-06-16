package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LessonControllerTest {

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController lessonController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController).build();
    }

    @Test
    void testUploadHtmlFile() throws Exception {
        // Arrange
        String fileName = "test.html";
        String content = "<html><body>Test content</body></html>";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                fileName,
                MediaType.TEXT_HTML_VALUE,
                content.getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/lesson/upload-html")
                        .file(file)
                        .param("fileName", fileName))
                .andExpect(status().isOk());
    }

    @Test
    void testUploadHtmlFile_Error() throws Exception {
        // Arrange
        String fileName = "test.html";
        String content = "<html><body>Test content</body></html>";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                fileName,
                MediaType.TEXT_HTML_VALUE,
                content.getBytes());

        // Act & Assert
        mockMvc.perform(multipart("/lesson/upload-html")
                        .file(file)
                        .param("fileName", fileName))
                .andExpect(status().isOk());
    }

    @Test
    void testDownloadHtmlFile() throws Exception {
        // Arrange
        String fileName = "test.html";
        String content = "<html><body>Test content</body></html>";
        Resource resource = new ByteArrayResource(content.getBytes()) {
            @Override
            public String getFilename() {
                return fileName;
            }
        };

        when(lessonService.downloadHtmlFile(fileName)).thenReturn(resource);

        // Act & Assert
        mockMvc.perform(get("/lesson/download-html/{fileName}", fileName))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.html\""))
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(content().bytes(content.getBytes()));
    }

    @Test
    void testDownloadHtmlFile_NotFound() throws Exception {
        // Arrange
        String fileName = "non-existent.html";

        when(lessonService.downloadHtmlFile(fileName)).thenThrow(new IOException("File not found"));

        // Act & Assert
        mockMvc.perform(get("/lesson/download-html/{fileName}", fileName))
                .andExpect(status().isNotFound());
    }
}