package com.elvira.programming_platform.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "lesson") // уникає рекурсії
@EqualsAndHashCode(exclude = "lesson") // уникає рекурсії
@Entity
@Table(name = "theory")
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
