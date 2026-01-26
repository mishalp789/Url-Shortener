package com.mishal.urlshortener.dto;

import java.time.LocalDateTime;

public class ShortUrlResponse {

    private String shortCode;
    private String longUrl;
    private LocalDateTime createdAt;

    public ShortUrlResponse(String shortCode,String longUrl,LocalDateTime createdAt){
        this.shortCode = shortCode;
        this.longUrl = longUrl;
        this.createdAt = createdAt;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
