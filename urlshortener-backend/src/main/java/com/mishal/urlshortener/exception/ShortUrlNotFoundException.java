package com.mishal.urlshortener.exception;

public class ShortUrlNotFoundException extends RuntimeException{

    public ShortUrlNotFoundException(String message){
        super(message);
    }
}
