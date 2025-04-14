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

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();
}
