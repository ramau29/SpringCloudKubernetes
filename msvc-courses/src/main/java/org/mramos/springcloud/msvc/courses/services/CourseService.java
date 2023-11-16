package org.mramos.springcloud.msvc.courses.services;

import org.mramos.springcloud.msvc.courses.models.User;
import org.mramos.springcloud.msvc.courses.models.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> listAll();
    Optional<Course> byId(Long id);
    Optional<Course> byIdWithUsers(Long id);
    Course save(Course course);
    void delete(Long id);

    Optional<User> assignUser(User user, Long courseId);
    Optional<User> createUser(User user, Long courseId);
    Optional<User> removeUser(User user, Long courseId);

    void deleteCourseUserById(Long id);

}
