package org.mramos.springcloud.msvc.courses.repositories;

import org.mramos.springcloud.msvc.courses.entity.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {


}
