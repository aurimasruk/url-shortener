package com.example.urlshortener.service;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UrlServiceTest {
    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUrlWithNewUrl(){
        String originalUrl = "https://example.com";
        String shortenedUrl = "abcd123";

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortenedUrl(shortenedUrl);

        when(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.empty());
        when(urlRepository.save(any(Url.class))).thenReturn(url);

        Url savedUrl = urlService.saveUrl(originalUrl);

        assertNotNull(savedUrl);
        assertEquals(originalUrl, savedUrl.getOriginalUrl());
        assertNotNull(url.getShortenedUrl());

    }

    @Test
    void testSaveUrlWithExistingUrl(){
        String originalUrl = "https://example.com";
        String shortenedUrl = "abcd123";
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortenedUrl(shortenedUrl);

        when(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.of(url));

        Url savedUrl = urlService.saveUrl(originalUrl);

        assertNotNull(savedUrl);
        assertEquals(originalUrl, savedUrl.getOriginalUrl());
        assertEquals(shortenedUrl, savedUrl.getShortenedUrl());

        verify(urlRepository, never()).save(any(Url.class));
    }

    @Test
    void testGetOriginalUrlWhenShortenedUrlExists() {
        String originalUrl = "https://example.com";
        String shortenedUrl = "abcd123";

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortenedUrl(shortenedUrl);

        when(urlRepository.findByShortenedUrl(shortenedUrl)).thenReturn(Optional.of(url));

        Optional<Url> result = urlService.getOriginalUrl(shortenedUrl);

        assertTrue(result.isPresent());
        assertEquals(originalUrl, result.get().getOriginalUrl());
    }

    @Test
    void testGetOriginalUrlWhenShortenedUrlDoesNotExist() {
        String shortenedUrl = "nonExistentShortUrl";

        when(urlRepository.findByShortenedUrl(shortenedUrl)).thenReturn(Optional.empty());
        Optional<Url> result = urlService.getOriginalUrl(shortenedUrl);
        assertFalse(result.isPresent());
    }
}
