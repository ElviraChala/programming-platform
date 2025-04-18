package com.elvira.programming_platform.repository;

import com.elvira.programming_platform.model.ProgrammingTask;
import org.springframework.data.repository.CrudRepository;

public interface ProgrammingTaskRepository extends CrudRepository<ProgrammingTask, Long> {
}
