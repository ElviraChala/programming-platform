package com.elvira.programming_platform.coverter;

import com.elvira.programming_platform.dto.CourseDTO;
import com.elvira.programming_platform.model.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseConverter {
    public Course toModel(CourseDTO source) {
        Course target = new Course();
        target.setId(source.getId());
        target.setName(source.getName());
        return target;
    }

    public CourseDTO toDTO(Course source) {
        CourseDTO target = new CourseDTO();
        target.setId(source.getId());
        target.setName(source.getName());
        return target;
    }
}
