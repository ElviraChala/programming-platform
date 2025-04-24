package com.elvira.programming_platform.controller;

import com.elvira.programming_platform.dto.TheoryDTO;
import com.elvira.programming_platform.service.TheoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/theory")
public class TheoryController {
    private final TheoryService theoryService;

    public TheoryController(TheoryService theoryService) {
        this.theoryService = theoryService;
    }

    @PostMapping
    public ResponseEntity<TheoryDTO> createTheory(@RequestBody TheoryDTO theoryDTO) {
        return ResponseEntity.ok(theoryService.createTheory(theoryDTO));
    }

    @GetMapping
    public ResponseEntity<TheoryDTO> readTheoryById(@RequestParam Long id) {
        return ResponseEntity.ok(theoryService.readTheoryById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TheoryDTO>> readAllTheories() {
        return ResponseEntity.ok(theoryService.readAllTheories());
    }

    @PutMapping
    public ResponseEntity<TheoryDTO> updateTheory(@RequestBody TheoryDTO theoryDTO) {
        TheoryDTO updated = theoryService.updateTheory(theoryDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTheory(@RequestParam Long id) {
        theoryService.deleteTheory(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
