package com.elvira.programming_platform.coverter;

import com.elvira.programming_platform.dto.StudentDTO;
import com.elvira.programming_platform.model.Student;

public class StudentConverter {
    public Student toModel(StudentDTO source) {
        Student target = new Student();
        target.setId(source.getId());
        target.setEmail(source.getEmail());
        target.setUsername(source.getUsername());
        target.setName(source.getName());
        target.setPassword(source.getPassword());
        target.setEmail(source.getEmail());
        target.setRole(source.getRole());
        target.setScore(source.getScore());
//        target.setCourses(source.getCoursesId());
        return target;
    }

    public StudentDTO toDTO(Student source) {
        StudentDTO target = new StudentDTO();
        target.setId(source.getId());
        target.setEmail(source.getEmail());
        target.setUsername(source.getUsername());
        target.setName(source.getName());
        target.setPassword(source.getPassword());
        target.setEmail(source.getEmail());
        target.setRole(source.getRole());
        target.setScore(source.getScore());
//        target.setCourses(source.getCoursesId());
        return target;
    }
}
