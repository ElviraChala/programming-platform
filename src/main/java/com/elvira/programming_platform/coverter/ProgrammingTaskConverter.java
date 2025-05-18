package com.elvira.programming_platform.coverter;

import com.elvira.programming_platform.dto.ProgrammingTaskDTO;
import com.elvira.programming_platform.model.ProgrammingTask;
import com.elvira.programming_platform.repository.LessonRepository;
import org.springframework.stereotype.Component;

@Component
public class ProgrammingTaskConverter {

    private final LessonRepository lessonRepository;

    public ProgrammingTaskConverter(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public ProgrammingTask toModel(ProgrammingTaskDTO source) {
        ProgrammingTask target = new ProgrammingTask();
        if (source.getId() != null && source.getId() > 0) {
            target.setId(source.getId());
        }
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setStarterCode(source.getStarterCode());
        target.setExpectedOutput(source.getExpectedOutput());
        target.setLesson(lessonRepository.findById(source.getLessonId()).orElse(null));
        return target;
    }

    public ProgrammingTaskDTO toDTO(ProgrammingTask source) {
        ProgrammingTaskDTO target = new ProgrammingTaskDTO();
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setStarterCode(source.getStarterCode());
        target.setExpectedOutput(source.getExpectedOutput());
        target.setLessonId(source.getLesson().getId());
        return target;
    }
}
