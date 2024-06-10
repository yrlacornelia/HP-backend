package com.example.hpbackend.controller;

import com.example.hpbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @GetMapping("/login")
    public String user() {
        return "Welcome, authenticated user!";
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticationRequest =
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        try {
            Authentication authenticationResponse =
                    this.authenticationManager.authenticate(authenticationRequest);
            // Authentication successful
            // You can generate a token or perform any other necessary actions here
            String token = generateToken(authenticationResponse); // Example method to generate a token
            return ResponseEntity.ok().body(token);
        } catch (AuthenticationException e) {
            // Authentication failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    // Method to generate token (Example)
    private String generateToken(Authentication authenticationResponse) {
        return "GeneratedToken";
    }

    public record LoginRequest(String username, String password) {
    }


}

