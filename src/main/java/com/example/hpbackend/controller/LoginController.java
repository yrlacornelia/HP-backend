package com.example.hpbackend.controller;
import com.example.hpbackend.repositories.UserRepository;
import com.example.hpbackend.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LoginController {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthService authService;
    public LoginController(UserRepository userRepository, AuthService authService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }


    @GetMapping("/")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok("{\"message\": \"Welcome to the API\"}");
    }
    @GetMapping("/csrf-token")
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }
    @GetMapping("/userLoggedIn")
    public ResponseEntity<?> checkUserLoggedIn(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {

            return ResponseEntity.ok().body(Map.of("loggedIn", true));
        } else {
            System.out.println("hello" + authentication);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("loggedIn", false));
        }
    }

}
