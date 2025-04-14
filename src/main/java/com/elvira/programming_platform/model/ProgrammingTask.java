package com.elvira.programming_platform.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "programming_task")
@Data
public class ProgrammingTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String description;

    private String starterCode;

    private String expectedOutput;
}
