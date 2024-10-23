package com.example.urlshortener.controller;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UrlController {
    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String originalUrl) {
        try {
            originalUrl = originalUrl.replace("\"","");
            Url savedUrl = urlService.saveUrl(originalUrl);
            return ResponseEntity.ok(savedUrl.getShortenedUrl());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{shortenedUrl}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortenedUrl) {
        return urlService.getOriginalUrl(shortenedUrl)
                .map(url -> ResponseEntity.ok(url.getOriginalUrl()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: shortened url not found."));
    }
    // 301 redirect..
}
