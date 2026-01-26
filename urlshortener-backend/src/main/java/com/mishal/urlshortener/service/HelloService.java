package com.mishal.urlshortener.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HelloService {

    public Map<String,String> getMessage(){
        return Map.of(
                "message", "URL Shortener Backend is running"
        );
    }

}
