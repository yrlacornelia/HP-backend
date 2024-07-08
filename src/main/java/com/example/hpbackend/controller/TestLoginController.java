package com.example.hpbackend.controller;


import com.example.hpbackend.entity.User;
import com.example.hpbackend.repositories.UserRepository;
import com.example.hpbackend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class TestLoginController {
    private final UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    private final ResourceLoader resourceLoader;

    private final AuthService authService;

    public TestLoginController(UserRepository userRepository, ResourceLoader resourceLoader, AuthService authService) {
        this.userRepository = userRepository;
        this.resourceLoader = resourceLoader;
        this.authService = authService;
    }

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";


    @PostMapping("/uploadProfileImage")
    public ResponseEntity<?> uploadProfileImage(@RequestParam("image") MultipartFile file) throws IOException {
        String username = authService.getCurrentUser();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        user.setImageData(file.getBytes());
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}
