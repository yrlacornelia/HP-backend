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

        request.getSession().invalidate();
        request.getSession(true);

        Cookie customCookie = new Cookie("customCookieName", "customCookieValue");
        customCookie.setHttpOnly(true);
        customCookie.setSecure(true);
        customCookie.setPath("/");
        customCookie.setMaxAge(3600);
        response.addCookie(customCookie);
    }
}
