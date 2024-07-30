package com.example.hpbackend.controller;

import com.example.hpbackend.config.ImageUploader;
import com.example.hpbackend.entity.User;
import com.example.hpbackend.entity.Event;
import com.example.hpbackend.repositories.EventRepository;
import com.example.hpbackend.repositories.UserRepository;
import com.example.hpbackend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AdminController {
    private final UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    private final ResourceLoader resourceLoader;

    private final AuthService authService;
    ImageUploader imageUploader;

    private EventRepository eventRepository;

    public AdminController(UserRepository userRepository, ImageUploader imageUploader, EventRepository eventRepository,ResourceLoader resourceLoader, AuthService authService) {
        this.userRepository = userRepository;
        this.imageUploader = imageUploader;
        this.resourceLoader = resourceLoader;
        this.authService = authService;
    }

    @GetMapping("/allusers")
    List<User> userList() {
        return userRepository.findAll();
    }


    // create user
    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user.getImageData() == null){

            user.setImageData(imageUploader.uploadImage());
        }
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }

    // delete user
    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
    }

    // all users filter by house

    // search by username

    // create event
    @PostMapping("/createEvent")
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        eventRepository.save(event);
        return ResponseEntity.noContent().build();
    }

    // delete event
    @DeleteMapping("/event/{id}")
    public void deleteEvent(@PathVariable("id") Long id) {
        eventRepository.deleteById(id);
    }


    // delete event after the time


}
