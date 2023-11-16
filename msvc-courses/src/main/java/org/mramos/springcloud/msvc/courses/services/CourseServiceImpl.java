package org.mramos.springcloud.msvc.courses.services;

import org.mramos.springcloud.msvc.courses.clients.UserClientRest;
import org.mramos.springcloud.msvc.courses.models.User;
import org.mramos.springcloud.msvc.courses.models.entity.Course;
import org.mramos.springcloud.msvc.courses.models.entity.CourseUser;
import org.mramos.springcloud.msvc.courses.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository repository;
    @Autowired
    private UserClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Course> listAll() {
        return (List<Course>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> byId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> byIdWithUsers(Long id) {
        Optional<Course> optCourse = repository.findById(id);
        if (optCourse.isPresent()){
            Course course = optCourse.get();
            if (!course.getCourseUsers().isEmpty()) {
                List<Long> ids = course.getCourseUsers().stream().map(CourseUser::getUserId).toList();
                List<User> users = client.getUsersByCourse(ids);
                course.setUsers(users);
            }
            return Optional.of(course);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Course save(Course course) {
        return repository.save(course);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }


    @Override
    @Transactional
    public Optional<User> assignUser(User user, Long courseId) {
        Optional<Course> optionalCourse = repository.findById(courseId);
        if (optionalCourse.isPresent()){
            User userMsvc = client.detail(user.getId());
            Course course = optionalCourse.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMsvc.getId());
            course.addCourseUser(courseUser);
            repository.save(course);
            return Optional.of(userMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> createUser(User user, Long courseId) {
        Optional<Course> optionalCourse = repository.findById(courseId);
        if (optionalCourse.isPresent()){
            User newUserMsvc = client.create(user);
            Course course = optionalCourse.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(newUserMsvc.getId());
            course.addCourseUser(courseUser);
            repository.save(course);
            return Optional.of(newUserMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> removeUser(User user, Long courseId) {
        Optional<Course> optionalCourse = repository.findById(courseId);
        if (optionalCourse.isPresent()){
            User userMsvc = client.detail(user.getId());
            Course course = optionalCourse.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMsvc.getId());
            course.removeCourseUser(courseUser);
            repository.save(course);
            return Optional.of(userMsvc);
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public void deleteCourseUserById(Long id) {
        repository.deleteCourseUserById(id);
    }
}
