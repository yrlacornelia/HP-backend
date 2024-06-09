package com.example.hpbackend.controller;


import com.example.hpbackend.entity.User;
import com.example.hpbackend.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestLoginController {
    private final UserRepository userRepository;

    public TestLoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/home")
    public String home() {
        return "Welcome to the home page!";
    }

    @GetMapping("/user")
    public String user() {
        return "Welcome, authenticated user!";
    }
    @GetMapping("/allusers")
    List<User> userList() {
        return userRepository.findAll();
    }
}
