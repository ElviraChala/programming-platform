package com.elvira.programming_platform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "lesson")
@Data
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "order_index")
    private int orderIndex;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToOne(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Theory theory;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private CheckKnowledge checkKnowledge;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ProgrammingTask> programmingTasks;
}
