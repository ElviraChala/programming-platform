package com.elvira.programming_platform.repository;

import com.elvira.programming_platform.model.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Long> {
}
