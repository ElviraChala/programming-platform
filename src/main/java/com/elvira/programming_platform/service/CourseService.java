package com.elvira.programming_platform.service;

import com.elvira.programming_platform.coverter.CourseConverter;
import com.elvira.programming_platform.dto.CourseDTO;
import com.elvira.programming_platform.model.Course;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.model.enums.Level;
import com.elvira.programming_platform.model.enums.Role;
import com.elvira.programming_platform.repository.CourseRepository;
import com.elvira.programming_platform.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseConverter courseConverter;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository, CourseConverter courseConverter, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.courseConverter = courseConverter;
        this.studentRepository = studentRepository;
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
                .toList();
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

    public List<CourseDTO> readAllCoursesByStudentLevel(String username) {
        Student student = studentRepository.findByUsername(username).orElse(null);
        if (student == null || student.getRole() == Role.ADMIN) {
            return readAllCourses();
        }

        Level studentLevel = student.getLevel();
        List<Course> allCourses = (List<Course>) courseRepository.findAll();

        // Filter courses based on student's level (return courses with level <= student's level)
        List<Course> filteredCourses = allCourses.stream()
                .filter(course -> course.getLevel().ordinal() <= studentLevel.ordinal())
                .toList();

        return filteredCourses.stream()
                .map(courseConverter::toDTO)
                .toList();
    }
}
