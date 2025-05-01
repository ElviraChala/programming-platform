package com.elvira.programming_platform.service.check;

import com.elvira.programming_platform.coverter.FirstCheckKnowledgeConverter;
import com.elvira.programming_platform.dto.check.AnswerDTO;
import com.elvira.programming_platform.dto.check.CheckEvaluationResultDTO;
import com.elvira.programming_platform.dto.check.FirstCheckKnowledgeDTO;
import com.elvira.programming_platform.model.FirstCheckKnowledge;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.model.enums.Level;
import com.elvira.programming_platform.repository.QuestionRepository;
import com.elvira.programming_platform.repository.StudentRepository;
import com.elvira.programming_platform.repository.check.FirstCheckKnowledgeRepository;
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

    public CheckEvaluationResultDTO checkAnswer(List<AnswerDTO> answerDTOS) {
        double totalScore = 0;

        int lowScore = 0;
        int mediumScore = 0;
        int highScore = 0;

        int lowQuestionCount = 0;
        int mediumQuestionCount = 0;
        int highQuestionCount = 0;

        double lowScorePercentage = 0;
        double mediumScorePercentage = 0;
        double highScorePercentage = 0;

        FirstCheckKnowledgeDTO check = getFirstCheckKnowledge();
        List<Long> questionIds = check.getQuestionIds();

        String answer;
        String correctAnswer;
        Level level;

        for (int i = 0; i < answerDTOS.size(); i++) {
            answer = answerDTOS.get(i).getCurrentAnswer();
            level = questionRepository.findById(questionIds.get(i)).orElseThrow().getLevel();
            correctAnswer = questionRepository.findById(questionIds.get(i)).orElseThrow().getCorrectAnswer();
            switch (level) {
                case LOW:
                    lowQuestionCount++;
                    break;
                case MEDIUM:
                    mediumQuestionCount++;
                    break;
                case HIGH:
                    highQuestionCount++;
            }


            if (answer.equals(correctAnswer)) {
                switch (level) {
                    case LOW:
                        lowScore++;
                        break;
                    case MEDIUM:
                        mediumScore++;
                        break;
                    case HIGH:
                        highScore++;
                        break;
                }
            }
        }

        lowScorePercentage = lowScore * 100.0 / lowQuestionCount;
        mediumScorePercentage = mediumScore * 100.0 / mediumQuestionCount;
        highScorePercentage = highScore * 100.0 / highQuestionCount;

        int maxScore = getFirstCheckKnowledge().getQuestionIds().size();
        totalScore = (lowScore + mediumScore + highScore) * 100.0 / maxScore;

        return new CheckEvaluationResultDTO(totalScore,
                lowScorePercentage,
                mediumScorePercentage,
                highScorePercentage);
    }


    public Level setLevel(String studentUsername, double lowScore, double mediumScore, double highScore) {
        Student student = studentRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Level newLevel = Level.LOW;

        if (mediumScore > 75) {
            newLevel = Level.MEDIUM;
            if (highScore > 75) {
                newLevel = Level.HIGH;
            }
        }

        student.setLevel(newLevel);
        student.setIsFirst(false);
        studentRepository.save(student);
        return newLevel;
    }
}
