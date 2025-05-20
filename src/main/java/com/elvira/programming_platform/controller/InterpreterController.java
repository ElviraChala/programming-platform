package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.check.InterpreterRequest;
import com.elvira.programming_platform.dto.check.InterpreterResponse;
import com.elvira.programming_platform.service.InterpreterService;
import com.elvira.programming_platform.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/interpreter")
public class InterpreterController {

    private final InterpreterService interpreterService;
    private final StudentService studentService;

    @PostMapping()
    public ResponseEntity<InterpreterResponse> runInterpreter(@RequestParam("id") Long id,
                                                              @RequestParam(value = "studentId", required = false) Long studentId,
                                                              @RequestBody InterpreterRequest request) {
        InterpreterResponse response = interpreterService.run(id, request);

        // If the solution is correct and studentId is provided, mark the task as completed
        if (response.getIsOk() && studentId != null) {
            studentService.addCompletedTask(studentId, id);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/complete-task")
    public ResponseEntity<Void> completeTask(@RequestParam("studentId") Long studentId,
                                             @RequestParam("taskId") Long taskId) {
        studentService.addCompletedTask(studentId, taskId);
        return ResponseEntity.ok().build();
    }
}
