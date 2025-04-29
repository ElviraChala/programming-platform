package com.elvira.programming_platform.model;

import com.elvira.programming_platform.model.enums.Level;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "student")
@Data
public class Student extends User {
    @Column(name = "score")
    private int score = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses;

    private Boolean isFirst = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level = Level.LOW;
}
