package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.LessonConverter;
import com.elvira.programming_platform.dto.LessonDTO;
import com.elvira.programming_platform.model.Lesson;
import com.elvira.programming_platform.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {
    private final LessonConverter lessonConverter;
    private final LessonRepository lessonRepository;

    public LessonService(LessonConverter lessonConverter, LessonRepository lessonRepository) {
        this.lessonConverter = lessonConverter;
        this.lessonRepository = lessonRepository;
    }

    public LessonDTO createLesson(LessonDTO lessonDTO) {
        Lesson lessonModel = lessonConverter.toModel(lessonDTO);
        Lesson savedLesson = lessonRepository.save(lessonModel);
        return lessonConverter.toDTO(savedLesson);
    }

    public LessonDTO readLessonById(Long id) {
        Lesson findLesson = lessonRepository.findById(id).orElse(null);
        return findLesson == null ? null :
                lessonConverter.toDTO(findLesson);
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
}
