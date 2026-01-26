package com.mishal.urlshortener.controller;

import com.mishal.urlshortener.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {
    private final HelloService helloService;
    public HelloController(HelloService helloService){
        this.helloService = helloService;
    }
    @GetMapping("/hello")
    public Map<String,String> hello(){
        return helloService.getMessage();
    }
}
