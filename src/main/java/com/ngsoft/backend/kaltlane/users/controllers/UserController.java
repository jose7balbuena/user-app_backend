package com.ngsoft.backend.kaltlane.users.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.ngsoft.backend.kaltlane.users.entites.User;
import com.ngsoft.backend.kaltlane.users.services.IUserService;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;







@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping
    public List<User> list() {
        return userService.findAll();
    }

    @GetMapping("/page/{page}")
    public Page<User> geListPageable(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        return userService.findAll(pageable);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(user.orElseThrow());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User not found with id: " + id));
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        User createdUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    


    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validation(result);
        }

        Optional<User> existingUser = userService.findById(id);
        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setName(user.getName());
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setLastname(user.getLastname());
            userToUpdate.setSecondlastname(user.getSecondlastname());
            userToUpdate.setUsername(user.getUsername());

            User updatedUser = userService.save(userToUpdate);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User not found with id: " + id));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(
            error -> errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
