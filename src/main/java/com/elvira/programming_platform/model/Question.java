package com.elvira.programming_platform.model;

import com.elvira.programming_platform.model.enums.Level;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "question")
@Getter
@Setter
public class Question {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text")
    private String text;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> options;

    @Column(name = "correct_answer")
    private String correctAnswer;

    @ManyToOne
    @JoinColumn(name = "check_knowledge_id")
    private CheckKnowledge checkKnowledge;

    @Column(name = "level", nullable = false)
    @Enumerated(EnumType.STRING)
    private Level level;
}
