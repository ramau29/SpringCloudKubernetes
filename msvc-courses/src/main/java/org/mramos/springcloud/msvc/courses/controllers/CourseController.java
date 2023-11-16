package org.mramos.springcloud.msvc.courses.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import org.mramos.springcloud.msvc.courses.models.User;
import org.mramos.springcloud.msvc.courses.models.entity.Course;
import org.mramos.springcloud.msvc.courses.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CourseController {

    @Autowired
    private CourseService service;

    @GetMapping
    public ResponseEntity<List<Course>> listAll(){
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> courseDetails(@PathVariable Long id) {
        Optional<Course> optCourse = service.byIdWithUsers(id); //service.byId(id);
        if(optCourse.isPresent()){
            return ResponseEntity.ok(optCourse.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid  @RequestBody Course course, BindingResult result){
        if(result.hasErrors()){
            return validate(result);
        }
        Course courseDb = service.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@RequestBody Course course, BindingResult result, @PathVariable Long id){
        if(result.hasErrors()){
            return validate(result);
        }
        Optional<Course> optCourse = service.byId(id);
        if (optCourse.isPresent()){
            Course courseDb = optCourse.get();
            courseDb.setName(course.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Course> opt = service.byId(id);
        if (opt.isPresent()) {
            service.delete(id);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/assign-user/{courseId}")
    public ResponseEntity<?> assignUser(@RequestBody User user, @PathVariable Long courseId){
        Optional<User> optionalUser;
        try {
            optionalUser = service.assignUser(user, courseId);
        } catch (FeignException fe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Unable to create user or there is a communication " +
                            "error: " + fe.getMessage()));
        }
        if (optionalUser.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUser.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create-user/{courseId}")
    public ResponseEntity<?> createUser(@RequestBody User user, @PathVariable Long courseId){
        Optional<User> optionalUser;
        try {
            optionalUser = service.createUser(user, courseId);
        } catch (FeignException fe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "User does not exists by id or there is a communication " +
                            "error: " + fe.getMessage()));
        }
        if (optionalUser.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUser.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/remove-user/{courseId}")
    public ResponseEntity<?> removeUser(@RequestBody User user, @PathVariable Long courseId){
        Optional<User> optionalUser;
        try {
            optionalUser = service.removeUser(user, courseId);
        } catch (FeignException fe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "User does not exists by id or there is a communication " +
                            "error: " + fe.getMessage()));
        }
        if (optionalUser.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(optionalUser.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteCourseUser(@PathVariable Long id){
        service.deleteCourseUserById(id);
        return ResponseEntity.noContent().build();
    }










    private static ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "Error in the field '" + err.getField() + "': " + err.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
