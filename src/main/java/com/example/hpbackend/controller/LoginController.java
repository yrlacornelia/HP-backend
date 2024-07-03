package com.example.hpbackend.controller;

import com.example.hpbackend.entity.User;
import com.example.hpbackend.repositories.UserRepository;
import com.example.hpbackend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LoginController(UserRepository userRepository, AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public static record LoginRequest(String username, String password) {
    }

    public static record LoginResponse(String token, String username) {
    }



    // in use
    @GetMapping("/currentuser")
    public ResponseEntity<Object> getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsernameFromJWT(token);
            String hello = jwtTokenProvider.extractClaims(token).getId();


            String usernamee = authService.getCurrentUsername();
            User user = userRepository.findByUsername(usernamee);
          // return ResponseEntity.ok().body("Token is valid. Current user: " + username + hello + usernamee);
            return ResponseEntity.ok().body(user);

        }else {
            // Return an error response with status 401 UNAUTHORIZED
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }


/*    @GetMapping("/allusers")
    public ResponseEntity<List<User>> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok().body(users);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }*

 */

    // in use
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticationRequest =
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        try {
            Authentication authenticationResponse =
                    this.authenticationManager.authenticate(authenticationRequest);

            SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
            String token = jwtTokenProvider.generateToken(authenticationResponse);
            String username = authenticationResponse.getName();
            LoginResponse loginResponse = new LoginResponse(token, username);

            return ResponseEntity.ok().body(loginResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }
}
