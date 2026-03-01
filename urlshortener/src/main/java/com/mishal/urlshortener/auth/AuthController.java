package com.mishal.urlshortener.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");

        if ("admin".equals(username) && "password".equals(password)) {
            return Map.of(
                    "token", jwtUtil.generateToken(username, "ADMIN")
            );
        }

        if ("user".equals(username) && "password".equals(password)) {
            return Map.of(
                    "token", jwtUtil.generateToken(username, "USER")
            );
        }

        throw new RuntimeException("Invalid credentials");
    }
}
