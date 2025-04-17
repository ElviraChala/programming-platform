package com.elvira.programming_platform.repository;

import com.elvira.programming_platform.model.Lesson;
import org.springframework.data.repository.CrudRepository;

public interface LessonRepository extends CrudRepository<Lesson, Long> {
}
