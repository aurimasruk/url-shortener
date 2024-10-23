package com.example.urlshortener.controller;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UrlController.class)
public class UrlControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UrlService urlService;

    @BeforeEach
    void setUp() {  //initialize
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOriginalUrlWhenShortenedUrlExists() throws Exception {
        String shortenedUrl = "abcd123";
        String originalUrl = "https://example.com";

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortenedUrl(shortenedUrl);

        when(urlService.getOriginalUrl(shortenedUrl)).thenReturn(Optional.of(url));

        mvc.perform(get("/api/{shortenedUrl}", shortenedUrl))
                .andExpect(status().isOk())
                .andExpect(content().string(originalUrl));
    }

    @Test
    void testGetOriginalUrlWhenShortenedUrlDoesNotExist() throws Exception {
        String shortenedUrl = "nonExistentShortenedUrl";

        when(urlService.getOriginalUrl(shortenedUrl)).thenReturn(Optional.empty());

        mvc.perform(get("/api/{shortenedUrl}", shortenedUrl))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Error: shortened url not found."));
    }

    @Test
    void testShortenUrlWithValidUrl() throws Exception {
        String shortenedUrl = "abcd123";
        String originalUrl = "https://example.com";

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortenedUrl(shortenedUrl);

        when(urlService.saveUrl(originalUrl)).thenReturn(url);

        mvc.perform(post("/api/shorten")
                        .content(originalUrl)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(shortenedUrl));
    }

    @Test
    void testShortenUrlWithInvalidUrl() throws Exception {
        String invalidUrl = "invalid-url";

        when(urlService.saveUrl(invalidUrl)).thenThrow(new IllegalArgumentException("Invalid URL"));

        mvc.perform(post("/api/shorten")
                        .content(invalidUrl)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid URL"));
    }


}
