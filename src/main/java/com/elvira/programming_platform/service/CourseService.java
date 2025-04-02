package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.CourseConverter;
import com.elvira.programming_platform.dto.CourseDTO;
import com.elvira.programming_platform.model.Course;
import com.elvira.programming_platform.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseConverter courseConverter;

    public CourseService(CourseRepository courseRepository, CourseConverter courseConverter) {
        this.courseRepository = courseRepository;
        this.courseConverter = courseConverter;
    }

    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course courseModel = courseConverter.toModel(courseDTO);
        Course createdCourse = courseRepository.save(courseModel);
        return courseConverter.toDTO(createdCourse);
    }

    public CourseDTO readCourseById(Long userId) {
        Course findCourse = courseRepository.findById(userId).orElse(null);
        if (findCourse == null) {
            return null;
        }
        return courseConverter.toDTO(findCourse);
    }

    public List<CourseDTO> readAllCourses() {
        List<Course> findCourses = (List<Course>) courseRepository.findAll();
        return findCourses.stream()
                .map(courseConverter::toDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO updateCourse(CourseDTO newCourseDTO) {
        Long userId = newCourseDTO.getId();
        Course findCourse = courseRepository.findById(userId).orElse(null);
        if (findCourse == null) {
            return null;
        }

        Course courseModel = courseConverter.toModel(newCourseDTO);
        Course updatedCourse = courseRepository.save(courseModel);
        return courseConverter.toDTO(updatedCourse);
    }

    public void deleteCourse(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        }
    }
}
