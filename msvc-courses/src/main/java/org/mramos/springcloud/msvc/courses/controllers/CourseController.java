package org.mramos.springcloud.msvc.courses.controllers;

import jakarta.validation.Valid;
import org.mramos.springcloud.msvc.courses.entity.Course;
import org.mramos.springcloud.msvc.courses.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CourseController {

    @Autowired
    private CourseService service;

    @GetMapping
    public ResponseEntity<List<Course>> listAll(){
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> courseDetails(@PathVariable Long id){
        Optional<Course> optCourse = service.byId(id);
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

    private static ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "Error in the field '" + err.getField() + "': " + err.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
