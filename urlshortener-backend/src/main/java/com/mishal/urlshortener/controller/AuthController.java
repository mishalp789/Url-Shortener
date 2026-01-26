package com.mishal.urlshortener.controller;

import com.mishal.urlshortener.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController  {

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody Map<String,String> request){
        if("admin".equals(request.get("username"))&&
        "admin123".equals(request.get("password"))){
            String token = JwtUtil.generateToken(
                    "admin",
                    List.of("ROLE_ADMIN")
            );

            return Map.of("token",token);
        }
        throw new RuntimeException("Invalid credentials");
    }
}
