package com.mishal.urlshortener.url;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUrlRepository  extends JpaRepository<Url,String> {
}
