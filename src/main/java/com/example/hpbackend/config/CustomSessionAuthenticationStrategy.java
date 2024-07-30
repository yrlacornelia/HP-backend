package com.example.hpbackend.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

@Component
public class CustomSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response)
            throws SessionAuthenticationException {
        if (authentication == null) {
            throw new SessionAuthenticationException("Authentication object is null");
        }

        // Invalidate the current session and create a new one
        request.getSession().invalidate();
        request.getSession(true);

        // Add a custom cookie
        Cookie customCookie = new Cookie("customCookieName", "customCookieValue");
        customCookie.setHttpOnly(true); // Make the cookie HTTP only for security
        customCookie.setSecure(true); // Make the cookie secure if you're using HTTPS
        customCookie.setPath("/"); // Set the path for the cookie
        customCookie.setMaxAge(3600); // Set the maximum age of the cookie in seconds (1 hour)
        response.addCookie(customCookie);
    }
}
