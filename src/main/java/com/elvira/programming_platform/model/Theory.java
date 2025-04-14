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

    @Lob
    @Column(name = "content")
    private String content;

    @OneToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
