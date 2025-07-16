package com.assignment.assignment.services;

import com.assignment.assignment.models.User;
import com.assignment.assignment.payload.Response.JwtResponse;
import com.assignment.assignment.security.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // Set authentication in context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        String username = null;
        String email = null;
        Long userId = null;
        List<String> roles = new ArrayList<>();

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            username = userDetails.getUsername();
            email = userDetails.getEmail();
            userId = userDetails.getId();
            roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

        } else if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            username = oidcUser.getFullName(); // or getName()
            email = oidcUser.getEmail();
            roles = oidcUser.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Optional: lookup userId if needed
            // userId = userService.findOrCreateUser(oidcUser.getEmail()).getId();
        } else {
            throw new IllegalStateException("Unknown principal type: " + principal.getClass().getName());
        }

        User user = new User(email, email);

        userService.saveUser(user);

        // Build the redirect URL to frontend with JWT token
        String frontendRedirectUrl = "http://localhost:3000/callback"
                + "?JwtToken=" + URLEncoder.encode(jwt, StandardCharsets.UTF_8);

        // Perform redirect
        response.sendRedirect(frontendRedirectUrl);
    }
}
