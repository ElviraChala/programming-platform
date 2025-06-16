package com.elvira.programming_platform.model;

import com.elvira.programming_platform.model.enums.Level;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
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
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_passed_tests",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "check_knowledge_id")
    )
    private Set<CheckKnowledge> passedTests = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "student_completed_tasks",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "programming_task_id")
    )
    private Set<ProgrammingTask> completedTasks = new HashSet<>();

    public void setPassedTests(Set<CheckKnowledge> passedTests) {
        this.passedTests = passedTests;
        updateScore();
    }

    public void addPassedTest(CheckKnowledge checkKnowledge) {
        this.passedTests.add(checkKnowledge);
        updateScore();
    }

    public boolean removePassedTest(CheckKnowledge checkKnowledge) {
        boolean removed = this.passedTests.remove(checkKnowledge);
        if (removed) {
            updateScore();
        }
        return removed;
    }

    public void setCompletedTasks(Set<ProgrammingTask> completedTasks) {
        this.completedTasks = completedTasks;
        updateScore();
    }

    public void addCompletedTask(ProgrammingTask programmingTask) {
        this.completedTasks.add(programmingTask);
        updateScore();
    }

    public boolean removeCompletedTask(ProgrammingTask programmingTask) {
        boolean removed = this.completedTasks.remove(programmingTask);
        if (removed) {
            updateScore();
        }
        return removed;
    }

    private Boolean isFirst = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level = Level.LOW;

    public int getScore() {
        updateScore();
        return score;
    }

    public void updateScore() {
        int testsScore = 0;
        int tasksScore = 0;

        if (passedTests != null && !passedTests.isEmpty()) {
            testsScore = passedTests.stream()
                    .mapToInt(CheckKnowledge::getTestWeight)
                    .sum();
        }

        if (completedTasks != null && !completedTasks.isEmpty()) {
            tasksScore = completedTasks.stream()
                    .mapToInt(ProgrammingTask::getTestWeight)
                    .sum();
        }

        this.score = testsScore + tasksScore;
    }
}
