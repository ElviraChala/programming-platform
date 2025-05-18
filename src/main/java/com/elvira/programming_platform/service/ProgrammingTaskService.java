package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.ProgrammingTaskConverter;
import com.elvira.programming_platform.dto.ProgrammingTaskDTO;
import com.elvira.programming_platform.model.ProgrammingTask;
import com.elvira.programming_platform.repository.ProgrammingTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProgrammingTaskService {
    private final ProgrammingTaskConverter programmingTaskConverter;
    private final ProgrammingTaskRepository programmingTaskRepository;

    public ProgrammingTaskService(ProgrammingTaskConverter programmingTaskConverter, ProgrammingTaskRepository programmingTaskRepository) {
        this.programmingTaskConverter = programmingTaskConverter;
        this.programmingTaskRepository = programmingTaskRepository;
    }

    public ProgrammingTaskDTO createProgrammingTask(ProgrammingTaskDTO programmingTaskDTO) {
        ProgrammingTask programmingTaskModel = programmingTaskConverter.toModel(programmingTaskDTO);
        ProgrammingTask savedProgrammingTask = programmingTaskRepository.save(programmingTaskModel);
        return programmingTaskConverter.toDTO(savedProgrammingTask);
    }

    public ProgrammingTaskDTO readProgrammingTaskById(Long userId) {
        ProgrammingTask findProgrammingTask = programmingTaskRepository.findById(userId).orElse(null);
        if (findProgrammingTask == null) {
            return null;
        }
        return programmingTaskConverter.toDTO(findProgrammingTask);
    }

    public List<ProgrammingTaskDTO> readAllProgrammingTasks() {
        List<ProgrammingTask> findProgrammingTasks = (List<ProgrammingTask>) programmingTaskRepository.findAll();
        return findProgrammingTasks.stream()
                .map(programmingTaskConverter::toDTO)
                .toList();
    }

    public ProgrammingTaskDTO updateProgrammingTask(ProgrammingTaskDTO newProgrammingTaskDTO) {
        Long userId = newProgrammingTaskDTO.getId();
        ProgrammingTask findProgrammingTask = programmingTaskRepository.findById(userId).orElse(null);
        if (findProgrammingTask == null) {
            return null;
        }

        ProgrammingTask programmingTaskModel = programmingTaskConverter.toModel(newProgrammingTaskDTO);
        ProgrammingTask updatedProgrammingTask = programmingTaskRepository.save(programmingTaskModel);
        return programmingTaskConverter.toDTO(updatedProgrammingTask);
    }

    public void deleteProgrammingTask(Long id) {
        ProgrammingTask programmingTask = programmingTaskRepository.findById(id).orElse(null);
        if (programmingTask != null) {
            log.info("delete programming task id:{}", id);
            // Break the relationship with the lesson before deleting
            programmingTask.setLesson(null);
            // Save the updated programming task to persist the change
            programmingTaskRepository.save(programmingTask);
            // Now delete the programming task
            programmingTaskRepository.deleteById(id);
        } else {
            log.info("delete programming task id:{} not found", id);
        }
    }
}
