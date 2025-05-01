package com.elvira.programming_platform.coverter;

import com.elvira.programming_platform.dto.CourseDTO;
import com.elvira.programming_platform.model.Course;
import com.elvira.programming_platform.model.Lesson;
import com.elvira.programming_platform.model.Student;
import com.elvira.programming_platform.repository.LessonRepository;
import com.elvira.programming_platform.repository.StudentRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CourseConverter {

    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;

    public CourseConverter(StudentRepository studentRepository, LessonRepository lessonRepository) {
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
    }

    public Course toModel(CourseDTO source) {
        Course target = new Course();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setDescription(source.getDescription());

        if (source.getStudentIds() != null) {
            Set<Student> students = source.getStudentIds()
                    .stream()
                    .map(id -> studentRepository.findById(id).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            target.setStudents(students);
        }

        if(source.getLessonIds() != null){
            Set<Lesson> lessons = source.getLessonIds()
                    .stream()
                    .map(id-> lessonRepository.findById(id).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            target.setLessons(lessons);
        }

        target.setLevel(source.getLevel());

        return target;
    }

    public CourseDTO toDTO(Course source) {
        CourseDTO target = new CourseDTO();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setDescription(source.getDescription());

        if (source.getStudents() != null) {
            Set<Long> studentIds = source.getStudents()
                    .stream()
                    .map(Student::getId)
                    .collect(Collectors.toSet());
            target.setStudentIds(studentIds);
        }

        if (source.getLessons() != null) {
            Set<Long> lessonIds = source.getLessons()
                    .stream()
                    .map(Lesson::getId)
                    .collect(Collectors.toSet());
            target.setLessonIds(lessonIds);
        }

        target.setLevel(source.getLevel());


        return target;
    }
}
