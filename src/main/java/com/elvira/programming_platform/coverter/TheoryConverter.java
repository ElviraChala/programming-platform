package com.elvira.programming_platform.coverter;

import com.elvira.programming_platform.dto.TheoryDTO;
import com.elvira.programming_platform.model.Theory;
import com.elvira.programming_platform.repository.LessonRepository;
import org.springframework.stereotype.Component;

@Component
public class TheoryConverter {

    private final LessonRepository lessonRepository;

    public TheoryConverter(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public Theory toModel(TheoryDTO source) {
        Theory target = new Theory();
        if (source.getId() != null && source.getId() > 0) {
            target.setId(source.getId());
        }
        target.setFileName(source.getFileName());
        target.setLesson(lessonRepository.findById(source.getLessonId()).orElse(null));
        return target;
    }

    public TheoryDTO toDTO(Theory source) {
        TheoryDTO target = new TheoryDTO();
        target.setId(source.getId());
        target.setFileName(source.getFileName());
        target.setLessonId(source.getLesson().getId());
        return target;
    }

}
