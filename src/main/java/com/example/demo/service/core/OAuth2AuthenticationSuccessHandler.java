package com.example.demo.service.core;

import com.example.demo.entity.Role;
import com.example.demo.security.CustomOAuth2User;
import com.example.demo.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

        // Get the role from authorities
        String role = oauthUser.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER")
                .replace("ROLE_", "");

        // Generate token
        String token = jwtUtil.generateToken(oauthUser.getUsername(), Role.valueOf(role));

        // Set JWT in HTTP-only cookie
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false) // true in production
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 1 week
                .sameSite("Lax")
                .domain("localhost") // Change for production
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        // Create response DTO
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("username", oauthUser.getUsername());
        responseBody.put("role", role);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), responseBody);
        // Set response headers and write JSON
        /*response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        response.getWriter().flush();*/
    }
}
