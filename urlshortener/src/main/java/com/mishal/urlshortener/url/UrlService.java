package com.mishal.urlshortener.url;


import java.time.LocalDateTime;


import com.mishal.urlshortener.analytics.UrlClickedEvent;
import com.mishal.urlshortener.infrastructure.id.Base62Encoder;
import com.mishal.urlshortener.infrastructure.id.SnowFlakeIdGenerator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mishal.urlshortener.common.ShortCodeCollisionException;
import com.mishal.urlshortener.common.UrlExpiredException;
import com.mishal.urlshortener.common.UrlNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UrlService {
    
    private final UrlRepository urlRepository;
    private static final Logger log = LoggerFactory.getLogger(UrlService.class);
    private final SnowFlakeIdGenerator idGenerator;
    private final ApplicationEventPublisher eventPublisher;

    public UrlService(UrlRepository urlRepository,SnowFlakeIdGenerator idGenerator,ApplicationEventPublisher eventPublisher){
        this.urlRepository = urlRepository;
        this.idGenerator = idGenerator;
        this.eventPublisher = eventPublisher;
    }

    public String createShortUrl(String originalUrl,LocalDateTime expiresAt,String customAlias){
        String shortCode;
        log.info("Creating short URL for {}", originalUrl);
        if (customAlias!=null && !customAlias.isBlank()) {
            if(urlRepository.exists(customAlias)){
                throw new ShortCodeCollisionException("Custom alias already in use");

            }
            shortCode = customAlias;
        }
        else{
            int attempts = 0;
            do{
                long id = idGenerator.nextId();
                shortCode = Base62Encoder.encode(id);
                attempts++;
            } while(urlRepository.exists(shortCode) && attempts<3);
            if(urlRepository.exists(shortCode)){
                throw new ShortCodeCollisionException("Failed to generate unique short code");
            }
        
        }
        Url url = new Url(shortCode,originalUrl,expiresAt);
        urlRepository.save(url);
        log.info("Short URL created: {}", shortCode);
        return shortCode;
    }
    @Cacheable(value = "urls", key = "#shortCode")
    public String loadOriginalUrl(String shortCode) {

        Url url = urlRepository.find(shortCode);

        if (url == null) {
            throw new UrlNotFoundException(shortCode);
        }

        if (url.isExpired()) {
            log.warn("Expired URL accessed: {}", shortCode);
            throw new UrlExpiredException(shortCode);
        }

        return url.getOriginalUrl();
    }

    public String getOriginalUrl(String shortCode){

        log.info("Redirect requested for shortCode={}", shortCode);

        String originalUrl = loadOriginalUrl(shortCode);

        // ALWAYS publish event (even on cache hit)
        eventPublisher.publishEvent(new UrlClickedEvent(shortCode));

        log.debug("Click event published for {}", shortCode);

        return originalUrl;
    }

    public Url getUrl(String shortCode){
        Url url = urlRepository.find(shortCode);
        if(url == null){
            throw new UrlNotFoundException(shortCode);
        }

        return url;
    }

    public Page<Url> getAllUrls(Pageable pageable){
        return urlRepository.findAll(pageable);
    }
}
