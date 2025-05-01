package com.elvira.programming_platform.repository;

import com.elvira.programming_platform.model.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findAllByCheckKnowledgeId(Long checkKnowledgeId);
}
