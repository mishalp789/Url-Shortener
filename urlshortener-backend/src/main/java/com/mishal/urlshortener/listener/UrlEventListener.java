package com.mishal.urlshortener.listener;

import com.mishal.urlshortener.event.UrlCreatedEvent;
import com.mishal.urlshortener.event.UrlDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UrlEventListener {

    private static final Logger log =
            LoggerFactory.getLogger(UrlEventListener.class);

    @Async
    @EventListener
    public void onUrlCreated(UrlCreatedEvent event) {
        log.info("EVENT: URL created shortCode={}", event.shortCode());
    }

    @Async
    @EventListener
    public void onUrlDeleted(UrlDeletedEvent event) {
        log.info("EVENT: URL deleted shortCode={}", event.shortCode());
    }
}
