package org.mramos.springcloud.msvc.users.controllers;

import jakarta.validation.Valid;
import org.mramos.springcloud.msvc.users.models.entity.User;
import org.mramos.springcloud.msvc.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public Map<String, List<User>> listAll(){
        return Collections.singletonMap("users", service.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id){
        Optional<User> userOptional = service.byId(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody User user, BindingResult result){
        if(result.hasErrors()){
            return validate(result);
        }
        if (service.existsByEmail(user.getEmail())){
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("Message", "User not created, same email already exist."));
        }
        return  ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@Valid @RequestBody User user, BindingResult result, @PathVariable Long id){
        if(result.hasErrors()){
            return validate(result);
        }
        Optional<User> optionalUser = service.byId(id);
        if (optionalUser.isPresent()){
            User userDB = optionalUser.get();
            if (!user.getEmail().isEmpty() && !user.getEmail().equalsIgnoreCase(userDB.getEmail()) && service.getByEmail(user.getEmail()).isPresent()){
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("Message", "User not updated, same email already exist."));
            }
            userDB.setName(user.getName());
            userDB.setEmail(user.getEmail());
            userDB.setPassword(user.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(userDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<User> userOptional = service.byId(id);
        if (userOptional.isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users-by-course")
    public ResponseEntity<?> getUsersByCourse(@RequestParam List<Long> ids){
        return ResponseEntity.ok(service.listByIds(ids));
    }

    private static ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "Error in the field '" + err.getField() + "': " + err.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }



}
