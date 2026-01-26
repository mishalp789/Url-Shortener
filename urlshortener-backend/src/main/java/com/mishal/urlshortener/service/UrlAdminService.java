package com.mishal.urlshortener.service;


import com.mishal.urlshortener.entity.ShortUrl;
import com.mishal.urlshortener.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;

@Service
public class UrlAdminService {
    private final ShortUrlRepository repository;

    public UrlAdminService(ShortUrlRepository repository){
        this.repository = repository;
    }

    public void writeCsv(PrintWriter writer){
        writer.println("shortCode,longUrl,createdAt");
        for(ShortUrl s:repository.findAllActive()){
            writer.printf(
                    "%s,%s,%s%n",
                    s.getShortCode(),
                    s.getLongUrl(),
                    s.getCreatedAt()
            );
        }
    }
}
