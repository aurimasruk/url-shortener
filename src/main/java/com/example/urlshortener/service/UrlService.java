package com.example.urlshortener.service;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.repository.UrlRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    private static final Logger logger = LoggerFactory.getLogger(UrlService.class);
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 7;

    // For URL validation
    private static final String URL_REGEX = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    private boolean isValidUrl(String url) {
        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }

    public Url saveUrl(String originalUrl) {
        originalUrl = originalUrl.trim();
        if(!isValidUrl(originalUrl)) {
            logger.error("Invalid URL provided: {}", originalUrl);
            throw new IllegalArgumentException("Invalid URL");
        }

        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(originalUrl);  // check if exists in DB
        if(existingUrl.isPresent()) {
            return existingUrl.get();
        }

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        String shortenedUrl = generateUniqueShortenedUrl();
        url.setShortenedUrl(shortenedUrl);

        return urlRepository.save(url); // saving to DB
    }

    public String generateUniqueShortenedUrl() {  // short URL generation
        String shortenedUrl = generateRandomString();
        while(urlRepository.findByShortenedUrl(shortenedUrl).isPresent()) {
            shortenedUrl = generateRandomString();
        }
        return shortenedUrl;
    }

    public String generateRandomString() {  // string generation
        StringBuilder shortenedUrl = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            shortenedUrl.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return shortenedUrl.toString();
    }

    public Optional<Url> getOriginalUrl(String shortenedUrl) {
        return urlRepository.findByShortenedUrl(shortenedUrl);      // fetching from DB
    }
}
