package com.elvira.programming_platform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "check_knowledge")
@Data
public class CheckKnowledge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "checkKnowledge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToOne(mappedBy = "checkKnowledge")
    private Lesson lesson;
}
