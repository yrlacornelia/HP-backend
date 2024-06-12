package com.example.hpbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticationRequest =
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        try {
            Authentication authenticationResponse =
                    this.authenticationManager.authenticate(authenticationRequest);

            // Set the authentication in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authenticationResponse);

            // Retrieve the roles from the Authentication object
            String roles = authenticationResponse.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .collect(Collectors.joining(", "));

            // Example token: using username as a token (for demonstration purposes)
            String token = authenticationResponse.getName();

            // Create a response that includes the token and roles
            LoginResponse loginResponse = new LoginResponse(token, roles);

            return ResponseEntity.ok().body(loginResponse);
        } catch (AuthenticationException e) {
            // Authentication failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    public record LoginRequest(String username, String password) {
    }

    public record LoginResponse(String token, String roles) {
    }
    @GetMapping("/currentuser")
    public ResponseEntity<String> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok().body("Current user: " + authentication.getName());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No authenticated user found");
        }
    }
}
