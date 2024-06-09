package com.example.hpbackend.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestLoginController {

    @GetMapping("/home")
    public String home() {
        return "Welcome to the home page!";
    }

    @GetMapping("/user")
    public String user() {
        return "Welcome, authenticated user!";
    }
}
