package com.elvira.programming_platform.coverter;

import com.elvira.programming_platform.dto.StudentDTO;
import com.elvira.programming_platform.model.Course;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.repository.CourseRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StudentConverter {
    private final CourseRepository courseRepository;

    public StudentConverter(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

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
        target.setIsFirst(source.getIsFirst());
        target.setLevel(source.getLevel());
        Set<Long> coursesId = source.getCoursesId();
        if (coursesId != null) {
            target.setCourses(coursesId.stream().map(id -> courseRepository.findById(id).orElse(null)).collect(Collectors.toSet()));
        }
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
        target.setIsFirst(source.getIsFirst());
        target.setLevel(source.getLevel());
        Set<Course> courses = source.getCourses();
        if (courses != null) {
            target.setCoursesId(courses.stream().map(Course::getId).collect(Collectors.toSet()));
        }
        return target;
    }
}
