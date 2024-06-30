package com.example.hpbackend.controller;


import com.example.hpbackend.dtos.EditUserForm;
import com.example.hpbackend.entity.User;
import com.example.hpbackend.repositories.UserRepository;
import com.example.hpbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@RestController
public class TestLoginController {
    private final UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    private final ResourceLoader resourceLoader;

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public TestLoginController(UserRepository userRepository, ResourceLoader resourceLoader, AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.resourceLoader = resourceLoader;
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    @GetMapping("/currentHouse")
    public ResponseEntity<List<User>> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok().body(users);

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/userSettings")
    public ResponseEntity<User> userPage() {
        String username = authService.getCurrentUsername();
        User user = userRepository.findByUsername(username);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/userSettins")
    public ResponseEntity<?> editUser(@RequestBody EditUserForm userForm) {
        String username = authService.getCurrentUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        user.setUserName(userForm.getUsername());
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/uploadProfileImage")
    public ResponseEntity<?> uploadProfileImage(@RequestParam("image") MultipartFile file) throws IOException {
        String username = authService.getCurrentUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        user.setImageData(file.getBytes());
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}
