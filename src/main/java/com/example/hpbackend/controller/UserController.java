package com.example.hpbackend.controller;

import com.example.hpbackend.dtos.EditUserForm;
import com.example.hpbackend.repositories.UserRepository;
import com.example.hpbackend.entity.User;
import com.example.hpbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    private final ResourceLoader resourceLoader;

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserRepository userRepository, ResourceLoader resourceLoader, AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.resourceLoader = resourceLoader;
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

 public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    @GetMapping("/currentHouse")
    public ResponseEntity<String> currentHouse() {
        String hej = "hej";
        return ResponseEntity.ok().body(hej);
    }

    @GetMapping("/userSettings")
    public ResponseEntity<?> userPage() {
        String username = authService.getCurrentUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        String base64Image;
        if(user.getImageData() != null) {
             base64Image = Base64.getEncoder().encodeToString(user.getImageData());
        }
        else {
            try {
                Path imagePath = Path.of(resourceLoader.getResource("classpath:images/profile.jpeg").getURI());
                System.out.println(Arrays.toString(Files.readAllBytes(imagePath)));
                user.setImageData(Files.readAllBytes(imagePath));
                 base64Image = Base64.getEncoder().encodeToString(user.getImageData());
            } catch (IOException e) {
                e.printStackTrace();

                return null;
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("imageData", base64Image);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/userSettings")
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












/*
    @PostMapping("/createuser")
    ResponseEntity<Void> createUser(@RequestBody User user) {
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/allusers")
    List<User> userList() {
        return userRepository.findAll();
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
