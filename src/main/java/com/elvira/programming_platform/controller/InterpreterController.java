package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.check.InterpreterRequest;
import com.elvira.programming_platform.dto.check.InterpreterResponse;
import com.elvira.programming_platform.service.InterpreterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/interpreter")
public class InterpreterController {

    private final InterpreterService interpreterService;

    @PostMapping()
    public ResponseEntity<InterpreterResponse> runInterpreter(@RequestParam("id") Long id,
                                                              @RequestBody InterpreterRequest request) {
        InterpreterResponse response = interpreterService.run(id, request);

        return ResponseEntity.ok(response);
    }
}
