package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.ProgrammingTaskDTO;
import com.elvira.programming_platform.service.ProgrammingTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("programming-task")
public class ProgrammingTaskController {
    private final ProgrammingTaskService programmingTaskService;

    public ProgrammingTaskController(ProgrammingTaskService programmingTaskService) {
        this.programmingTaskService = programmingTaskService;
    }

    @PostMapping
    public ResponseEntity<ProgrammingTaskDTO> createProgrammingTask(@RequestBody ProgrammingTaskDTO programmingTaskDTO) {
        return ResponseEntity.ok(programmingTaskService.createProgrammingTask(programmingTaskDTO));
    }

    @GetMapping
    public ResponseEntity<ProgrammingTaskDTO> readProgrammingTaskById(@RequestParam Long id) {
        return ResponseEntity.ok(programmingTaskService.readProgrammingTaskById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProgrammingTaskDTO>> readAllProgrammingTasks() {
        return ResponseEntity.ok(programmingTaskService.readAllProgrammingTasks());
    }

    @PutMapping
    public ResponseEntity<ProgrammingTaskDTO> updateProgrammingTask(@RequestBody ProgrammingTaskDTO programmingTaskDTO) {
        ProgrammingTaskDTO updated = programmingTaskService.updateProgrammingTask(programmingTaskDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProgrammingTask(@RequestParam Long id) {
        programmingTaskService.deleteProgrammingTask(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
