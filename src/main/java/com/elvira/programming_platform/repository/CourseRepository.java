package com.elvira.programming_platform.repository;

import com.elvira.programming_platform.model.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {
}
