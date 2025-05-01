package com.elvira.programming_platform.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "theory")
@Data
public class Theory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "content")
    private String fileName;

    @OneToOne()
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
