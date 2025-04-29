package com.elvira.programming_platform.service.check;

import com.elvira.programming_platform.coverter.FirstCheckKnowledgeConverter;
import com.elvira.programming_platform.dto.check.FirstCheckKnowledgeDTO;
import com.elvira.programming_platform.dto.check.AnswerDTO;
import com.elvira.programming_platform.model.FirstCheckKnowledge;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.model.enums.Level;
import com.elvira.programming_platform.repository.check.FirstCheckKnowledgeRepository;
import com.elvira.programming_platform.repository.QuestionRepository;
import com.elvira.programming_platform.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FirstCheckKnowledgeService {

    private final FirstCheckKnowledgeRepository firstCheckKnowledgeRepository;
    private final FirstCheckKnowledgeConverter firstCheckKnowledgeConverter;
    private final QuestionRepository questionRepository;
    private final StudentRepository studentRepository;

    public FirstCheckKnowledgeService(FirstCheckKnowledgeRepository firstCheckKnowledgeRepository,
                                      FirstCheckKnowledgeConverter firstCheckKnowledgeConverter, QuestionRepository questionRepository, StudentRepository studentRepository) {
        this.firstCheckKnowledgeRepository = firstCheckKnowledgeRepository;
        this.firstCheckKnowledgeConverter = firstCheckKnowledgeConverter;
        this.questionRepository = questionRepository;
        this.studentRepository = studentRepository;
    }

    public FirstCheckKnowledgeDTO getFirstCheckKnowledge() {
        FirstCheckKnowledge check = firstCheckKnowledgeRepository.findTopByOrderByIdDesc().orElseThrow();
        return firstCheckKnowledgeConverter.toDTO(check);
    }

    public double checkAnswer(List<AnswerDTO> answerDTOS) {
        int score = 0;

        FirstCheckKnowledgeDTO check = getFirstCheckKnowledge();
        List<Long> questionIds = check.getQuestionIds();

        String answer;
        String correctAnswer;

        for (int i = 0; i < answerDTOS.size(); i++) {
            answer = answerDTOS.get(i).getCurrentAnswer();
            correctAnswer = questionRepository.findById(questionIds.get(i)).orElseThrow().getCorrectAnswer();
            if (answer.equals(correctAnswer)) {
                score++;
            }
        }

        int maxScore = getFirstCheckKnowledge().getQuestionIds().size();
        return score * 100.0 / maxScore;
    }

    public Level setLevel(String studentUsername, double score) {
        Student student = studentRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Level newLevel = Level.LOW;
        if (score >= 36 && score <= 75) {
            newLevel = Level.MEDIUM;
        }
        if (score >= 76 && score <= 100) {
            newLevel = Level.HIGH;
        }

        student.setLevel(newLevel);
        student.setIsFirst(false);
        studentRepository.save(student);
        return newLevel;
    }
}
