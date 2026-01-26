package com.mishal.urlshortener.controller;

import com.mishal.urlshortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class RedirectController {

    private final UrlShortenerService service;

    public RedirectController(UrlShortenerService service) {
        this.service = service;
    }

    @GetMapping("/r/{code}")
    public void redirect(
            @PathVariable String code,
            HttpServletResponse response
    ) throws IOException {

        String longUrl = service.getByShortCode(code).getLongUrl();
        response.sendRedirect(longUrl);
    }
}
