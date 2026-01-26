package com.mishal.urlshortener.controller;

import com.mishal.urlshortener.dto.ShortUrlResponse;
import com.mishal.urlshortener.dto.UrlShortenRequest;
import com.mishal.urlshortener.dto.UrlShortenResponse;
import com.mishal.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/shorten")
public class UrlShortenerController {
    private UrlShortenerService service;

    public UrlShortenerController(UrlShortenerService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UrlShortenResponse> shorten(@Valid @RequestBody UrlShortenRequest request){
        UrlShortenResponse response = service.shorten(request.getLongUrl());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{code}")
    public UrlShortenResponse get(@PathVariable String code){
        return service.getByShortCode(code);
    }

    @GetMapping
    public Page<ShortUrlResponse> getAll(
            @PageableDefault(size = 10, sort = "createdAt")
            Pageable pageable
    ) {
        return service.getAll(pageable);
    }

    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String code) {
        service.delete(code);
    }



}
