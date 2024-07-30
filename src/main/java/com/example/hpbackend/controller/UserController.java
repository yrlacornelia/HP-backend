package com.example.hpbackend.controller;

import com.example.hpbackend.config.ImageUploader;
import com.example.hpbackend.dtos.EditUserForm;
import com.example.hpbackend.repositories.UserRepository;
import com.example.hpbackend.entity.User;
import com.example.hpbackend.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    private final ResourceLoader resourceLoader;

    private final AuthService authService;
    ImageUploader imageUploader;

    public UserController(UserRepository userRepository, ImageUploader imageUploader, ResourceLoader resourceLoader, AuthService authService) {
        this.userRepository = userRepository;
        this.imageUploader = imageUploader;
        this.resourceLoader = resourceLoader;
        this.authService = authService;
    }

 public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";



    @PostMapping("/createNewPerson")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user.getImageData() == null){

            user.setImageData(imageUploader.uploadImage());
        }
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/userSettings")
    public ResponseEntity<?> userPage() {
        String username = authService.getCurrentUser();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/userSettings")
    public ResponseEntity<?> editUser(@RequestBody EditUserForm userForm) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in");
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        user.setUserName(userForm.getUsername());

        userRepository.save(user);
        Authentication request = new UsernamePasswordAuthenticationToken(userForm.getUsername(), user.getPassword());
        Authentication result = authenticationManager.authenticate(request);
        SecurityContextHolder.getContext().setAuthentication(result);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/uploadProfileImage")
    public ResponseEntity<?> uploadProfileImage(@RequestParam("image") MultipartFile file) throws IOException {
       System.out.println(file);
       System.out.println("HELLO");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in");
        }

        String username = authentication.getName();
        System.out.println(authentication.getName());
        User user = userRepository.findByUsername(username);
        user.setImageData(file.getBytes());
        userRepository.save(user);
        Path imagePath = Path.of(resourceLoader.getResource("classpath:static/images/profile.jpeg").getURI());
        Files.readAllBytes(imagePath);
        return ResponseEntity.ok(user);
    }
    @GetMapping("/currentuser")
    public  ResponseEntity<Object>  getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in");
        }
        String username = authentication.getName();
        System.out.println(authentication.getName());
        System.out.println(authentication.getAuthorities());

        User user = userRepository.findByUsername(username);
        return ResponseEntity.ok().body(user);

    }
    @GetMapping("/currenRole")
    public  ResponseEntity<Object>  getRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in");
        }
        String username = authentication.getName();
        System.out.println(authentication.getAuthorities());

        User user = userRepository.findByUsername(username);
        return ResponseEntity.ok().body(user);

    }





    // in use
    @GetMapping("/allusers")
    List<User> userList() {
        return userRepository.findAll();
    }
}












/*
    @PostMapping("/createuser")
    ResponseEntity<Void> createUser(@RequestBody User user) {
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }



    @GetMapping("/user/{id}")
    Optional user(@PathVariable("id") Long id) {
        return userRepository.findById(id);
    }

    @DeleteMapping("/deleteuser/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
    }

    @PatchMapping("/changeuser/{id}")
    ResponseEntity<Void> updateUser(@RequestBody User updatedUser, @PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setUserName(updatedUser.getUsername());
            userRepository.save(existingUser);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/
