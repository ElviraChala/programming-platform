package com.elvira.programming_platform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "question")
@Data
public class Question {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text")
    private String text;

    @ElementCollection
    private List<String> options;

    @Column(name = "correct_answer")
    private String correctAnswer;

    @ManyToOne
    @JoinColumn(name = "checkKnowledge_id")
    private CheckKnowledge checkKnowledge;
}
