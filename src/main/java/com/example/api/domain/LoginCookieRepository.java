package com.example.api.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginCookieRepository extends JpaRepository<LoginCookie, Long> {
    Optional<LoginCookie> findTop1ByCookieOrderByIdDesc(String cookie);
}
