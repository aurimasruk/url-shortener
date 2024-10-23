package com.example.urlshortener.repository;

import com.example.urlshortener.model.Url;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UrlRepository extends CrudRepository<Url, Long> {
    Optional<Url> findByShortenedUrl(String shortenedUrl);
    Optional<Url> findByOriginalUrl(String originalUrl);
}
