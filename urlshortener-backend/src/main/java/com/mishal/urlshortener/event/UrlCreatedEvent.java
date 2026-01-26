package com.mishal.urlshortener.event;

public record UrlCreatedEvent(String shortCode, String longUrl) {}
