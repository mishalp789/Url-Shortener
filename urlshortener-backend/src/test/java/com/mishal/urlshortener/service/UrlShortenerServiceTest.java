package com.mishal.urlshortener.service;

import com.mishal.urlshortener.entity.ShortUrl;
import com.mishal.urlshortener.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {

    @Mock
    private ShortUrlRepository repository;

    @InjectMocks
    private UrlShortenerService service;

    @Test
    void shouldCreateNewShortUrlWhenNotExists() {
        String longUrl = "https://example.com";

        when(repository.findByLongUrl(longUrl))
                .thenReturn(Optional.empty());

        when(repository.save(any(ShortUrl.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.shorten(longUrl);

        assertThat(response.getLongUrl()).isEqualTo(longUrl);
        assertThat(response.getShortCode()).isNotNull();

        verify(repository).save(any(ShortUrl.class));
    }
}
