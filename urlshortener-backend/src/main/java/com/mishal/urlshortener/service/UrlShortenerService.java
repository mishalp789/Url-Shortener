package com.mishal.urlshortener.service;

import com.mishal.urlshortener.dto.ShortUrlResponse;
import com.mishal.urlshortener.dto.UrlShortenResponse;
import com.mishal.urlshortener.entity.ShortUrl;
import com.mishal.urlshortener.event.UrlCreatedEvent;
import com.mishal.urlshortener.event.UrlDeletedEvent;
import com.mishal.urlshortener.exception.ShortUrlNotFoundException;
import com.mishal.urlshortener.repository.ShortUrlRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


@Service
public class UrlShortenerService {

   private final ShortUrlRepository repository;
   private static final Logger log = LoggerFactory.getLogger(UrlShortenerService.class);
   private final ApplicationEventPublisher eventPublisher;
   public UrlShortenerService(ShortUrlRepository repository,ApplicationEventPublisher eventPublisher){
       this.repository = repository;
       this.eventPublisher = eventPublisher;
   }

    @PreAuthorize("hasRole('ADMIN')")
    public UrlShortenResponse shorten(String longUrl) {
        log.info("Creating short URL for longUrl={}",longUrl);

        return repository.findByLongUrlAndActiveTrue(longUrl)
                .map(existing ->
                        new UrlShortenResponse(
                                existing.getShortCode(),
                                existing.getLongUrl()
                        )
                )
                .orElseGet(() -> {
                    String shortCode = generateShortCode();
                    ShortUrl entity = new ShortUrl(shortCode, longUrl);
                    repository.save(entity);
                    eventPublisher.publishEvent(
                            new UrlCreatedEvent(entity.getShortCode(), entity.getLongUrl())
                    );
                    return new UrlShortenResponse(shortCode, longUrl);
                });
    }


    @Cacheable(value = "shortUrls", key = "#p0")
    public UrlShortenResponse getByShortCode(String code){
        log.info("Cache MISS for shortCode={}",code);
        ShortUrl entity = repository.findByShortCodeAndActiveTrue(code)
                .orElseThrow(()->new ShortUrlNotFoundException("Short URL not found"));

        return new UrlShortenResponse(
                entity.getShortCode(),
                entity.getLongUrl());
    }

    private String generateShortCode(){
        return UUID.randomUUID().toString().substring(0,6);
    }

    public Page<ShortUrlResponse> getAll(Pageable pageable){
        return repository.findByActiveTrue(pageable)
                .map(entity -> new ShortUrlResponse(
                        entity.getShortCode(),
                        entity.getLongUrl(),
                        entity.getCreatedAt()
                ));
    }


    @CacheEvict(value = "shortUrls", key = "#shortCode")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String shortCode) {
        ShortUrl entity = repository.findByShortCodeAndActiveTrue(shortCode)
                .orElseThrow(() -> new ShortUrlNotFoundException("Short URL not found"));

        entity.setActive(false);
        repository.save(entity);
        eventPublisher.publishEvent(new UrlDeletedEvent(shortCode));
    }



}
