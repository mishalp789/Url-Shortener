package com.mishal.urlshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mishal.urlshortener.dto.UrlShortenRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRejectUnauthorizedRequest() throws Exception {
        mockMvc.perform(post("/shorten"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldCreateShortUrlWhenAuthorized() throws Exception {
        UrlShortenRequest request = new UrlShortenRequest();
        request.setLongUrl("https://spring.io");

        mockMvc.perform(post("/shorten")
                        .header("Authorization", "Bearer VALID_ADMIN_JWT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortCode").exists())
                .andExpect(jsonPath("$.longUrl").value("https://spring.io"));
    }
}
