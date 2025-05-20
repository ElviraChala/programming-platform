package com.elvira.programming_platform.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "programming_task")
@Getter
@Setter
public class ProgrammingTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "starter_code")
    private String starterCode;

    @Column(name = "real_output")
    private String realOutput;

    @Column(name = "expected_output")
    private String expectedOutput;

    @Column(name = "test_weight")
    private int testWeight;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    @JsonBackReference
    private Lesson lesson;

    @Override
    public String toString() {
        return "ProgrammingTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", starterCode='" + starterCode + '\'' +
                ", realOutput='" + realOutput + '\'' +
                ", expectedOutput='" + expectedOutput + '\'' +
                ", lesson=" + lesson +
                '}';
    }
}
