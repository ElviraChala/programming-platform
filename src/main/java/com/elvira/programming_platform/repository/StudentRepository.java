package com.elvira.programming_platform.repository;

import com.elvira.programming_platform.model.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
}
