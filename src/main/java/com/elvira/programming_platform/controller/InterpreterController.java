package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.check.InterpreterRequest;
import com.elvira.programming_platform.dto.check.InterpreterResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/interpreter")
public class InterpreterController {

    private static final Logger log = LoggerFactory.getLogger(InterpreterController.class);

    @PostMapping()
    public ResponseEntity<InterpreterResponse> runInterpreter(@RequestParam("id") Long id,
                                                              @RequestBody InterpreterRequest request) {
        String idValue = String.valueOf(id);
        log.info(idValue);
        log.info(request.getCore());

        InterpreterResponse response = new InterpreterResponse();
        response.setIsOk(true);
        response.setExpectedOutput("1");
        response.setActualOutput("1");

        return ResponseEntity.ok(response);
    }
}
