package com.elvira.programming_platform.repository;

import com.elvira.programming_platform.model.FirstCheckKnowledge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FirstCheckKnowledgeRepository extends CrudRepository<FirstCheckKnowledge, Long> {
    Optional<FirstCheckKnowledge> findTopByOrderByIdDesc();
}
