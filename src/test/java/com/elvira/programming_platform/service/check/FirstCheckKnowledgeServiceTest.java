package com.elvira.programming_platform.service.check;

import com.elvira.programming_platform.coverter.FirstCheckKnowledgeConverter;
import com.elvira.programming_platform.dto.check.AnswerDTO;
import com.elvira.programming_platform.dto.check.CheckEvaluationResultDTO;
import com.elvira.programming_platform.dto.check.FirstCheckKnowledgeDTO;
import com.elvira.programming_platform.model.FirstCheckKnowledge;
import com.elvira.programming_platform.model.Question;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.model.enums.Level;
import com.elvira.programming_platform.repository.QuestionRepository;
import com.elvira.programming_platform.repository.StudentRepository;
import com.elvira.programming_platform.repository.check.FirstCheckKnowledgeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FirstCheckKnowledgeServiceTest {

    private FirstCheckKnowledgeRepository firstCheckKnowledgeRepository;
    private FirstCheckKnowledgeConverter firstCheckKnowledgeConverter;
    private QuestionRepository questionRepository;
    private StudentRepository studentRepository;
    private FirstCheckKnowledgeService service;

    @BeforeEach
    void setUp() {
        firstCheckKnowledgeRepository = mock(FirstCheckKnowledgeRepository.class);
        firstCheckKnowledgeConverter = mock(FirstCheckKnowledgeConverter.class);
        questionRepository = mock(QuestionRepository.class);
        studentRepository = mock(StudentRepository.class);
        service = new FirstCheckKnowledgeService(
                firstCheckKnowledgeRepository,
                firstCheckKnowledgeConverter,
                questionRepository,
                studentRepository
        );
    }

    @Test
    void testGetFirstCheckKnowledge() {
        FirstCheckKnowledge check = new FirstCheckKnowledge();
        FirstCheckKnowledgeDTO dto = new FirstCheckKnowledgeDTO();

        when(firstCheckKnowledgeRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(check));
        when(firstCheckKnowledgeConverter.toDTO(check)).thenReturn(dto);

        FirstCheckKnowledgeDTO result = service.getFirstCheckKnowledge();

        assertEquals(dto, result);
        verify(firstCheckKnowledgeRepository).findTopByOrderByIdDesc();
        verify(firstCheckKnowledgeConverter).toDTO(check);
    }

    @Test
    void testCheckAnswer() {
        FirstCheckKnowledgeDTO dto = new FirstCheckKnowledgeDTO();
        dto.setQuestionIds(List.of(1L, 2L, 3L));

        when(firstCheckKnowledgeRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(new FirstCheckKnowledge()));
        when(firstCheckKnowledgeConverter.toDTO(any())).thenReturn(dto);

        Question q1 = new Question();
        q1.setLevel(Level.LOW);
        q1.setCorrectAnswer("a");
        Question q2 = new Question();
        q2.setLevel(Level.MEDIUM);
        q2.setCorrectAnswer("b");
        Question q3 = new Question();
        q3.setLevel(Level.HIGH);
        q3.setCorrectAnswer("c");

        when(questionRepository.findById(1L)).thenReturn(Optional.of(q1));
        when(questionRepository.findById(2L)).thenReturn(Optional.of(q2));
        when(questionRepository.findById(3L)).thenReturn(Optional.of(q3));

        List<AnswerDTO> answers = Arrays.asList(
                new AnswerDTO(1L, "a"), // correct low
                new AnswerDTO(2L, "x"), // incorrect medium
                new AnswerDTO(3L, "c")  // correct high
        );

        CheckEvaluationResultDTO result = service.checkAnswer(answers);

        assertEquals(66.66666666666666, result.getTotalScore(), 0.01);
        assertEquals(100.0, result.getLowScore(), 0.01);
        assertEquals(0.0, result.getMediumScore(), 0.01);
        assertEquals(100.0, result.getHighScore(), 0.01);
    }

    @Test
    void testSetLevel_LOW() {
        Student student = new Student();
        student.setLevel(Level.HIGH);
        student.setIsFirst(true);

        when(studentRepository.findByUsername("user")).thenReturn(Optional.of(student));

        Level result = service.setLevel("user", 50, 50, 50);

        assertEquals(Level.LOW, result);
        assertFalse(student.getIsFirst());
        verify(studentRepository).save(student);
    }

    @Test
    void testSetLevel_MEDIUM() {
        Student student = new Student();

        when(studentRepository.findByUsername("user")).thenReturn(Optional.of(student));

        Level result = service.setLevel("user", 50, 80, 40);

        assertEquals(Level.MEDIUM, result);
        verify(studentRepository).save(student);
    }

    @Test
    void testSetLevel_HIGH() {
        Student student = new Student();

        when(studentRepository.findByUsername("user")).thenReturn(Optional.of(student));

        Level result = service.setLevel("user", 50, 80, 90);

        assertEquals(Level.HIGH, result);
        verify(studentRepository).save(student);
    }

    @Test
    void testSetLevel_StudentNotFound() {
        when(studentRepository.findByUsername("user")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.setLevel("user", 0, 0, 0));
    }
}
