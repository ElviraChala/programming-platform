package com.elvira.programming_platform.service.check;

import com.elvira.programming_platform.coverter.CheckKnowledgeConverter;
import com.elvira.programming_platform.coverter.QuestionConverter;
import com.elvira.programming_platform.dto.QuestionDTO;
import com.elvira.programming_platform.dto.check.AnswerDTO;
import com.elvira.programming_platform.dto.check.CheckKnowledgeDTO;
import com.elvira.programming_platform.model.CheckKnowledge;
import com.elvira.programming_platform.model.Question;
import com.elvira.programming_platform.repository.check.CheckKnowledgeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckKnowledgeServiceTest {

    private CheckKnowledgeRepository checkKnowledgeRepository;
    private CheckKnowledgeConverter checkKnowledgeConverter;
    private QuestionConverter questionConverter;
    private CheckKnowledgeService service;

    @BeforeEach
    void setUp() {
        checkKnowledgeRepository = mock(CheckKnowledgeRepository.class);
        checkKnowledgeConverter = mock(CheckKnowledgeConverter.class);
        questionConverter = mock(QuestionConverter.class);
        service = new CheckKnowledgeService(checkKnowledgeRepository, checkKnowledgeConverter, questionConverter);
    }

    @Test
    void testCreateCheckKnowledge() {
        CheckKnowledgeDTO dto = new CheckKnowledgeDTO();
        CheckKnowledge model = new CheckKnowledge();
        CheckKnowledge savedModel = new CheckKnowledge();
        CheckKnowledgeDTO savedDTO = new CheckKnowledgeDTO();

        when(checkKnowledgeConverter.toModel(dto)).thenReturn(model);
        when(checkKnowledgeRepository.save(model)).thenReturn(savedModel);
        when(checkKnowledgeConverter.toDTO(savedModel)).thenReturn(savedDTO);

        CheckKnowledgeDTO result = service.createCheckKnowledge(dto);

        assertEquals(savedDTO, result);
        verify(checkKnowledgeRepository).save(model);
    }

    @Test
    void testReadCheckKnowledgeById_Found() {
        Long id = 1L;
        CheckKnowledge model = new CheckKnowledge();
        CheckKnowledgeDTO dto = new CheckKnowledgeDTO();

        when(checkKnowledgeRepository.findById(id)).thenReturn(Optional.of(model));
        when(checkKnowledgeConverter.toDTO(model)).thenReturn(dto);

        CheckKnowledgeDTO result = service.readCheckKnowledgeById(id);

        assertEquals(dto, result);
    }

    @Test
    void testReadCheckKnowledgeById_NotFound() {
        when(checkKnowledgeRepository.findById(1L)).thenReturn(Optional.empty());
        assertNull(service.readCheckKnowledgeById(1L));
    }

    @Test
    void testUpdateCheckKnowledge_Found() {
        Long id = 1L;
        CheckKnowledgeDTO dto = new CheckKnowledgeDTO();
        dto.setId(id);

        CheckKnowledge existing = new CheckKnowledge();
        existing.setQuestions(new ArrayList<>());

        CheckKnowledge fromDTO = new CheckKnowledge();
        fromDTO.setQuestions(List.of(new Question()));

        CheckKnowledge saved = new CheckKnowledge();
        CheckKnowledgeDTO resultDTO = new CheckKnowledgeDTO();

        when(checkKnowledgeRepository.findById(id)).thenReturn(Optional.of(existing));
        when(checkKnowledgeConverter.toModel(dto)).thenReturn(fromDTO);
        when(checkKnowledgeRepository.save(existing)).thenReturn(saved);
        when(checkKnowledgeConverter.toDTO(saved)).thenReturn(resultDTO);

        CheckKnowledgeDTO result = service.updateCheckKnowledge(dto);

        assertEquals(resultDTO, result);
        verify(checkKnowledgeRepository).save(existing);
    }

    @Test
    void testUpdateCheckKnowledge_NotFound() {
        CheckKnowledgeDTO dto = new CheckKnowledgeDTO();
        dto.setId(2L);

        when(checkKnowledgeRepository.findById(2L)).thenReturn(Optional.empty());

        assertNull(service.updateCheckKnowledge(dto));
    }

    @Test
    void testDeleteCheckKnowledge_Exists() {
        when(checkKnowledgeRepository.existsById(1L)).thenReturn(true);
        service.deleteCheckKnowledge(1L);
        verify(checkKnowledgeRepository).deleteById(1L);
    }

    @Test
    void testDeleteCheckKnowledge_NotExists() {
        when(checkKnowledgeRepository.existsById(1L)).thenReturn(false);
        service.deleteCheckKnowledge(1L);
        verify(checkKnowledgeRepository, never()).deleteById(any());
    }

    @Test
    void testGetQuestionsByCheckKnowledgeId_Found() {
        Question question = new Question();
        QuestionDTO questionDTO = new QuestionDTO();

        CheckKnowledge check = new CheckKnowledge();
        check.setQuestions(List.of(question));

        when(checkKnowledgeRepository.findById(1L)).thenReturn(Optional.of(check));
        when(questionConverter.toDTO(question)).thenReturn(questionDTO);

        List<QuestionDTO> result = service.getQuestionsByCheckKnowledgeId(1L);

        assertEquals(1, result.size());
        assertEquals(questionDTO, result.get(0));
    }

    @Test
    void testGetQuestionsByCheckKnowledgeId_NotFound() {
        when(checkKnowledgeRepository.findById(1L)).thenReturn(Optional.empty());
        List<QuestionDTO> result = service.getQuestionsByCheckKnowledgeId(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testEvaluateAnswers_AllCorrect() {
        Question q1 = new Question();
        q1.setCorrectAnswer("a");
        Question q2 = new Question();
        q2.setCorrectAnswer("b");
        Question q3 = new Question();
        q3.setCorrectAnswer("c");

        CheckKnowledge check = new CheckKnowledge();
        check.setQuestions(List.of(q1, q2, q3));

        List<AnswerDTO> answers = List.of(
                new AnswerDTO(1L, "a"),
                new AnswerDTO(2L, "b"),
                new AnswerDTO(3L, "c")
        );

        when(checkKnowledgeRepository.findById(1L)).thenReturn(Optional.of(check));

        double result = service.evaluateAnswers(1L, answers);

        assertEquals(3.0, result, 0.01);
    }

    @Test
    void testEvaluateAnswers_SomeIncorrect() {
        Question q1 = new Question();
        q1.setCorrectAnswer("a");
        Question q2 = new Question();
        q2.setCorrectAnswer("b");
        Question q3 = new Question();
        q3.setCorrectAnswer("c");

        CheckKnowledge check = new CheckKnowledge();
        check.setQuestions(List.of(q1, q2, q3));

        List<AnswerDTO> answers = List.of(
                new AnswerDTO(1L, "a"),
                new AnswerDTO(2L, "x"), // wrong
                new AnswerDTO(3L, "c")
        );

        when(checkKnowledgeRepository.findById(1L)).thenReturn(Optional.of(check));

        double result = service.evaluateAnswers(1L, answers);

        assertEquals(2.0, result, 0.1);
    }

    @Test
    void testEvaluateAnswers_NotFound() {
        when(checkKnowledgeRepository.findById(1L)).thenReturn(Optional.empty());
        double result = service.evaluateAnswers(1L, List.of());
        assertEquals(0.0, result);
    }
}
