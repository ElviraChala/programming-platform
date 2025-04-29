package com.elvira.programming_platform.coverter;

import com.elvira.programming_platform.dto.LessonDTO;
import com.elvira.programming_platform.model.Lesson;
import com.elvira.programming_platform.model.ProgrammingTask;
import com.elvira.programming_platform.repository.check.CheckKnowledgeRepository;
import com.elvira.programming_platform.repository.CourseRepository;
import com.elvira.programming_platform.repository.ProgrammingTaskRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LessonConverter {
    private final CourseRepository courseRepository;
    private final CheckKnowledgeRepository checkKnowledgeRepository;
    private final ProgrammingTaskRepository programmingTaskRepository;
    private final TheoryConverter theoryConverter;

    public LessonConverter(CourseRepository courseRepository, CheckKnowledgeRepository checkKnowledgeRepository, ProgrammingTaskRepository programmingTaskRepository, TheoryConverter theoryConverter) {
        this.courseRepository = courseRepository;
        this.checkKnowledgeRepository = checkKnowledgeRepository;
        this.programmingTaskRepository = programmingTaskRepository;
        this.theoryConverter = theoryConverter;
    }

    public Lesson toModel(LessonDTO source) {
        Lesson target = new Lesson();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setOrderIndex(source.getOrderIndex());
        target.setCourse(courseRepository.findById(source.getId()).orElse(null));
        target.setTheory(theoryConverter.toModel(source.getTheory()));
        target.setCheckKnowledge(checkKnowledgeRepository.findById(source.getId()).orElse(null));
        if (source.getCheckKnowledgeId() != null) {
            Set<ProgrammingTask> programmingTasks = source.getProgrammingTaskIds()
                    .stream()
                    .map(id -> programmingTaskRepository.findById(id).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            target.setProgrammingTasks(programmingTasks);
        }
        return target;
    }

    public LessonDTO toDTO(Lesson source) {
        LessonDTO target = new LessonDTO();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setOrderIndex(source.getOrderIndex());
        target.setCourseId(source.getCourse().getId());
        target.setTheory(theoryConverter.toDTO(source.getTheory()));
        target.setCheckKnowledgeId(source.getCheckKnowledge().getId());
        if (source.getProgrammingTasks() != null) {
            Set<Long> programmingTaskIds = source.getProgrammingTasks()
                    .stream()
                    .map(ProgrammingTask::getId)
                    .collect(Collectors.toSet());

            target.setProgrammingTaskIds(programmingTaskIds);
        }
        return target;
    }
}
