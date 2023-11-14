package org.mramos.springcloud.msvc.courses.services;

import org.mramos.springcloud.msvc.courses.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> listAll();
    Optional<Course> byId(Long id);
    Course save(Course course);
    void delete(Long id);
}
