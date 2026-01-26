package com.mishal.urlshortener.repository;

import com.mishal.urlshortener.entity.ShortUrl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByShortCodeAndActiveTrue(String shortCode);

    Optional<ShortUrl> findByLongUrlAndActiveTrue(String longUrl);
    Page<ShortUrl> findByActiveTrue(Pageable pageable);

    @Query("SELECT s FROM ShortUrl s WHERE s.active = true")
    List<ShortUrl> findAllActive();

}
