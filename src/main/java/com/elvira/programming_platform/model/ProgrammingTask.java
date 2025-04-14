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

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "starter_code")
    private String starterCode;

    @Column(name = "real_output")
    private String realOutput;

    private String expectedOutput;
}
