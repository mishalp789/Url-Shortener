package com.mishal.urlshortener.dto;

public class UrlShortenResponse {

    private String shortCode;
    private String longUrl;

    public UrlShortenResponse(String shortCode,String longUrl){
        this.shortCode = shortCode;
        this.longUrl = longUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getLongUrl() {
        return longUrl;
    }

}
